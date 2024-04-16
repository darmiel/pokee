package com.github.pokee.stick.request.parsers;

import com.github.pokee.stick.request.Request;
import com.github.pokee.stick.request.RequestHeader;

import java.io.BufferedReader;
import java.io.IOException;

// this is not a _real_ implementation of the http 1.1 version,
// but it checks if the `Host` header is present
public class RequestParserVersion1_1 extends RequestParserVersion1_0 {

    @Override
    public Request parse(final RequestHeader header, final BufferedReader reader) throws IOException {
        final Request request = super.parse(header, reader);
        if (!request.headers().contains("Host")) {
            throw new IllegalArgumentException("Host header is missing");
        }
        return request;
    }

}
