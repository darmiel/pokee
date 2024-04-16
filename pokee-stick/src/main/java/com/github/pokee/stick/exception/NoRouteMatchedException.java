package com.github.pokee.stick.exception;

public class NoRouteMatchedException extends IllegalArgumentException {

    private final String route;
    private final String part;

    public NoRouteMatchedException(final String route, final String part) {
        super("No route matched for: '" + route + "' and part: '" + part + "'");
        this.route = route;
        this.part =part;
    }

    public String getRoute() {
        return this.route;
    }

    public String getPart() {
        return this.part;
    }

}
