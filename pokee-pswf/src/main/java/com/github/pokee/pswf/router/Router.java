package com.github.pokee.pswf.router;

import com.github.pokee.pswf.request.Method;
import com.github.pokee.pswf.request.Request;

public interface Router {

    /**
     * Register a route with the given method and handlers.
     *
     * @param method  The method to register the route for.
     * @param route   The route to register.
     * @param handler The handlers to call when the route is matched.
     */
    void registerRoute(final Method method, final String route, final Handler handler);

    /**
     * Create a context for the given request.
     *
     * @param request The request to create the context for.
     * @return The context for the given request.
     */
    Context createContext(final Request request);

}
