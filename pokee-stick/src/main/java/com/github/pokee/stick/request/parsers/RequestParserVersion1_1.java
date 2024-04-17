package com.github.pokee.stick.request.parsers;

import com.github.pokee.stick.request.Request;
import com.github.pokee.stick.request.RequestHeader;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Extends {@link RequestParserVersion1_0} to implement specific parsing logic for HTTP/1.1 requests.
 * This class ensures that the 'Host' header is present in the request, which is a requirement in HTTP/1.1.
 */
public class RequestParserVersion1_1 extends RequestParserVersion1_0 {

    /**
     * Parses an HTTP/1.1 request, ensuring that the 'Host' header is included as per HTTP/1.1 specification.
     * If the 'Host' header is missing, an exception is thrown.
     *
     * @param header The {@link RequestHeader} object containing the initial request line information.
     * @param reader A {@link BufferedReader} from which the request headers and body are read.
     * @return A {@link Request} object populated with parsed values from the HTTP request.
     * @throws IOException              If an I/O error occurs during reading from the reader.
     * @throws IllegalArgumentException If the 'Host' header is missing in the HTTP/1.1 request.
     */
    @Override
    public Request parse(final RequestHeader header, final BufferedReader reader) throws IOException {
        final Request request = super.parse(header, reader);
        if (!request.headers().has("Host")) {
            throw new IllegalArgumentException("Host header is missing");
        }
        return request;
    }

}
