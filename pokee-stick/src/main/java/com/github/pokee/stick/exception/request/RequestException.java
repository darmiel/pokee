package com.github.pokee.stick.exception.request;

/**
 * Base class for exceptions related to HTTP request processing.
 */
public class RequestException extends Exception {

    public RequestException(String message) {
        super(message);
    }

}
