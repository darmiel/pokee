package com.github.pokee.stick.exception.request;

/**
 * Thrown when an expected content or response body is absent.
 * This exception typically indicates that the handling process failed to produce any output for the request.
 */
public class NoContentException extends RequestException {

    public NoContentException() {
        super("No content provided for the response");
    }

}
