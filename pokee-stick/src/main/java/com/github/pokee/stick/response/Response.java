package com.github.pokee.stick.response;

import com.github.pokee.stick.headers.Headers;

/**
 * Represents an immutable HTTP response. This record encapsulates all the necessary components of an HTTP response,
 * including the status code, status message, headers, and the body of the response.
 */
public record Response(int statusCode,  // The HTTP status code.
                       String statusMessage,  // The descriptive message associated with the status code.
                       Headers headers,  // The HTTP headers for the response.
                       byte[] body) {   // The body of the response as a byte array.

}
