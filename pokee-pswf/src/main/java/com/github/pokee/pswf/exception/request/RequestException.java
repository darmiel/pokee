package com.github.pokee.pswf.exception.request;

/**
 * Base class for exceptions related to HTTP request processing.
 */
public class RequestException extends RuntimeException {

    public RequestException(String message) {
        super(message);
    }

}
