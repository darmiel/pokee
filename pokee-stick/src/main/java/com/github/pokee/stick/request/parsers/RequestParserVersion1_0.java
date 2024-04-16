package com.github.pokee.stick.request.parsers;

import com.github.pokee.stick.headers.Headers;
import com.github.pokee.stick.request.*;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestParserVersion1_0 implements RequestParser {

    @Override
    public Request parse(final RequestHeader header, final BufferedReader reader) throws IOException {
        final Headers headers = new Headers();
        while (true) {
            final String line = reader.readLine();
            if (line.isBlank()) {
                break;
            }
            final int colonIndex = line.indexOf(':');

            final String key = line.substring(0, colonIndex);
            final String value = line.substring(colonIndex + 1).trim();
            headers.add(key, value);
        }
        return new Request(header.method(), header.path(), header.version(), headers.toImmutable(), reader);
    }

}
