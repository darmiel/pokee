package com.github.pokee.stick.request;

import com.github.pokee.stick.exception.RequestParseException;
import com.github.pokee.stick.headers.Headers;
import com.github.pokee.stick.request.parsers.RequestParser;
import com.github.pokee.stick.Method;
import com.github.pokee.stick.Version;

import java.io.BufferedReader;
import java.io.IOException;

public record Request(Method method,
                      String path,
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
