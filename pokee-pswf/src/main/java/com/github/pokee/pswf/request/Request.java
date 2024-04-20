package com.github.pokee.pswf.request;

import com.github.pokee.pswf.exception.RequestParseException;
import com.github.pokee.pswf.headers.Headers;
import com.github.pokee.pswf.request.parsers.RequestParser;

import java.io.BufferedReader;
import java.io.IOException;

public record Request(Method method,
                      String path,
                      String query,
                      Version version,
                      Headers headers,
                      BufferedReader reader) {

    /**
     * Read a request from a buffered reader
     *
     * @param reader the reader
     * @return the request
     * @throws IOException if an I/O error occurs
     */
    public static Request readRequest(final BufferedReader reader) throws IOException {
        // parse http method, path and version
        final RequestHeader header = RequestHeader.parseLine(reader.readLine().trim());
        if (!header.isValid()) {
            throw new RequestParseException("Invalid request header");
        }
        final RequestParser parser = header.version().getParser();
        if (parser == null) {
            throw new RequestParseException("Unsupported version: " + header.version());
        }
        return parser.parse(header, reader);
    }

    /**
     * @return the request formatted as a string
     */
    @Override
    public String toString() {
        return method + " " + path + " " + version;
    }

    /**
     * Pretty print the request
     *
     * @return the pretty printed request
     */
    public String prettyPrint() {
        return this.toString() + "\n" + headers.prettyPrint();
    }

}
