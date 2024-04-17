package com.github.pokee.stick.router;

import com.github.pokee.stick.Method;
import com.github.pokee.stick.exception.NoRouteMatchedException;
import com.github.pokee.stick.request.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A router that can handle parameterized routes.
 */
public class ParameterizableRouter implements Router {

    private final RouteNode root;

    public ParameterizableRouter() {
        this.root = new RouteNode("", false);
    }

    /**
     * Register a route with the given method and handlers.
     *
     * @param method  The method to register the route for.
     * @param route   The route to register.
     * @param handler The handlers to call when the route is matched.
     */
    @Override
    public void registerRoute(final Method method, final String route, final Handler handler) {
        final String[] parts = route.split("/");

        RouteNode current = this.root;
        for (final String part : parts) {
            if (part.isBlank()) {
                continue;
            }
            current = current.addChild(part);
        }
        current.addHandler(method, handler);
    }

    /**
     * Create a (parameterized) context for the given request.
     *
     * @param request The request to create the context for.
     * @return The context for the given request.
     */
    @Override
    public Context createContext(final Request request) {
        final Map<String, String> parameters = new HashMap<>();

        RouteNode current = this.root;

        final String[] parts = request.path().split("/");
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
                throw new NoRouteMatchedException(request.path(), part);
            }
            current = next;
        }

        final List<Handler> handlerList = current.getHandlers(request.method());
        if (handlerList == null || handlerList.isEmpty()) {
            throw NoRouteMatchedException.of(request);
        }

        return new Context(
                request,
                handlerList,
                parameters
        );
    }

}
