package com.github.pokee.pswf.request.parsers;

import com.github.pokee.pswf.request.Request;
import com.github.pokee.pswf.request.RequestHeader;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Defines the interface for parsing HTTP requests. Implementing classes are expected to convert
 * raw input from a {@link BufferedReader} into a {@link Request} object, using the details
 * provided in a {@link RequestHeader}.
 */
public interface RequestParser {

    /**
     * Parses an HTTP request from a given input stream and constructs a {@link Request} object based on the data read.
     * This method is responsible for reading the request headers and potentially the body, depending on the implementation.
     *
     * @param header The initial request header information including method, path, and HTTP version.
     * @param reader A {@link BufferedReader} from which the request's headers and potentially its body are read.
     * @return A {@link Request} object representing the parsed HTTP request.
     * @throws IOException If an error occurs during input reading.
     */
    Request parse(final RequestHeader header, final BufferedReader reader) throws IOException;

}
