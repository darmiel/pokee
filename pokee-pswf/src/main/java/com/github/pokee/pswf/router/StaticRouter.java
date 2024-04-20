package com.github.pokee.pswf.router;

import com.github.pokee.pswf.exception.NoRouteMatchedException;
import com.github.pokee.pswf.request.Method;
import com.github.pokee.pswf.request.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A router that can handle static routes.
 */
public class StaticRouter implements Router {

    private final Map<Method, Map<String, List<Handler>>> handlers;

    public StaticRouter() {
        this.handlers = new HashMap<>();
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
        final Map<String, List<Handler>> methodHandlers = this.handlers.computeIfAbsent(method, r -> new HashMap<>());
        final List<Handler> handlers = methodHandlers.computeIfAbsent(route, m -> new ArrayList<>());
        handlers.add(handler);
    }

    /**
     * Create a context for the given request.
     *
     * @param request The request to create the context for.
     * @return The context for the given request.
     */
    @Override
    public Context createContext(final Request request) {
        final Map<String, List<Handler>> handlersByPath = this.handlers.get(request.method());
        if (handlersByPath == null || !handlersByPath.containsKey(request.path())) {
            throw NoRouteMatchedException.of(request);
        }
        return new Context(request, List.copyOf(handlersByPath.get(request.path())), new HashMap<>());
    }

}
