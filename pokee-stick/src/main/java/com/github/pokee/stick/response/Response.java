package com.github.pokee.stick.response;

import com.github.pokee.stick.headers.Headers;

/**
 * Represents an immutable HTTP response. This record encapsulates all the necessary components of an HTTP response,
 * including the status code, status message, headers, and the body of the response.
 * It implements the {@link ResponseLike} interface allowing it to be returned directly from methods expecting a response-like type.
 */
public record Response(int statusCode,  // The HTTP status code.
                       String statusMessage,  // The descriptive message associated with the status code.
                       Headers headers,  // The HTTP headers for the response.
                       byte[] body) implements ResponseLike {

    @Override
    public Response extractResponse() {
        return this;
    }

}
