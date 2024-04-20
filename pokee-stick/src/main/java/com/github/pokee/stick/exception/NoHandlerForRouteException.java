package com.github.pokee.stick.exception;

/**
 * Thrown when no handler is found for a given route within the router configuration.
 * This typically means that the requested route is not defined in the server's routing settings.
 */
public class NoHandlerForRouteException extends IllegalArgumentException {
}
