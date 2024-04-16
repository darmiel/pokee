package com.github.pokee.stick.router;

import com.github.pokee.stick.Method;
import com.github.pokee.stick.exception.NoRouteMatchedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Router {

    private final RouteNode root;

    public Router() {
        this.root = new RouteNode("", false);
    }

    /**
     * Register a route with the given method and handlers.
     *
     * @param method   The method to register the route for.
     * @param route    The route to register.
     * @param handlers The handlers to call when the route is matched.
     */
    public void registerRoute(final Method method, final String route, final Handler... handlers) {
        final String[] parts = route.split("/");

        RouteNode current = this.root;
        for (final String part : parts) {
            if (part.isBlank()) {
                continue;
            }
            current = current.addChild(part);
        }
        for (final Handler handler : handlers) {
            current.addHandler(method, handler);
        }
    }

    /**
     * Find the routes for the given method and URL.
     *
     * @param method The method to find the routes for.
     * @param url    The URL to find the routes for.
     * @return The routes for the given method and URL.
     * @throws NoRouteMatchedException if no route is found for the given method and URL.
     */
    public RouterRunner findRoutes(final Method method, final String url) {
        final Map<String, String> parameters = new HashMap<>();

        RouteNode current = this.root;

        final String[] parts = url.split("/");
        for (final String part : parts) {
            if (part.isBlank()) {
                continue;
            }

            RouteNode next = current.getChild(part);
            if (next == null) {
                for (final RouteNode possibleChild : current.getChildren().values()) {
                    if (possibleChild.isParameter()) {
                        parameters.put(possibleChild.getPart(), part);
                        next = possibleChild;
                        break;
                    }
                }
            }

            if (next == null) {
                throw new NoRouteMatchedException(url, part);
            }

            current = next;
        }

        final List<Handler> handlerList = current.getHandlers(method);
        if (handlerList == null || handlerList.isEmpty()) {
            throw new NoRouteMatchedException(url, method.name());
        }

        return new RouterRunner(parameters, handlerList);
    }

}
