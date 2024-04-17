package com.github.pokee.stick.exception;

import java.io.IOException;

/**
 * Exception thrown when an error occurs during the parsing of an HTTP request.
 * This class extends {@link IOException} to provide an exception type specifically
 * for request parsing issues, allowing more targeted exception handling and error reporting.
 */
public class RequestParseException extends IOException {

    /**
     * Constructs a new RequestParseException with the specified detail message.
     *
     * @param message The detail message explaining the reason for the exception.
     */
    public RequestParseException(final String message) {
        super(message);
    }

}
