package com.github.pokee.stick.router;

import com.github.pokee.stick.request.Request;
import com.github.pokee.stick.response.Response;

/**
 * Defines the interface for handling errors that occur during the processing of HTTP requests.
 * Implementations of this interface should provide mechanisms to create a response based on
 * the exception encountered during request handling.
 */
public interface ErrorHandler {

    /**
     * Handles an exception or error that occurs during the processing of an HTTP request.
     *
     * @param request   The request during which the error occurred.
     * @param throwable The exception that was thrown during request processing.
     * @return A Response object representing the error state, typically containing error details
     * and appropriate HTTP status codes.
     */
    Response handle(final Request request, final Throwable throwable);

}
