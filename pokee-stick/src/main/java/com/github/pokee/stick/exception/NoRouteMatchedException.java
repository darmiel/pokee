package com.github.pokee.stick.exception;

import com.github.pokee.stick.request.Request;

/**
 * Exception thrown when a requested route does not match any defined routes.
 * This class extends {@link IllegalArgumentException} to indicate that the error is due to an incorrect
 * or inappropriate routing argument.
 */
public class NoRouteMatchedException extends IllegalArgumentException {

    private final String route;
    private final String part;

    /**
     * Constructs a new NoRouteMatchedException with a message detailing the unmatched route and part.
     *
     * @param route The route path attempted to be matched.
     * @param part  The specific part of the route that failed to match.
     */
    public NoRouteMatchedException(final String route, final String part) {
        super("No route matched for: '" + route + "' and part: '" + part + "'");
        this.route = route;
        this.part = part;
    }

    /**
     * Factory method to create a NoRouteMatchedException based on the details of a {@link Request}.
     *
     * @param request The request from which the route and method are extracted.
     * @return A new instance of NoRouteMatchedException tailored to the given request.
     */
    public static NoRouteMatchedException of(final Request request) {
        return new NoRouteMatchedException(request.path(), request.method().name());
    }

    /**
     * Returns the route path that was attempted to be matched.
     *
     * @return The unmatched route path.
     */
    public String getRoute() {
        return this.route;
    }

    /**
     * Returns the specific part of the route that failed to match.
     *
     * @return The part of the route that failed.
     */
    public String getPart() {
        return this.part;
    }

}
