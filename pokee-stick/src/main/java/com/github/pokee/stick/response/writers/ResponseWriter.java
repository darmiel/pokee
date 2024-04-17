package com.github.pokee.stick.response.writers;

import com.github.pokee.stick.response.Response;

import java.io.BufferedOutputStream;
import java.io.IOException;

/**
 * A response writer for HTTP responses. This class handles the formatting
 * and output of HTTP responses "according to the HTTP specification" (more or less).
 */
public interface ResponseWriter {

    /**
     * Writes an HTTP response to the specified BufferedOutputStream.
     *
     * @param response The HTTP response to write.
     * @param boss     The BufferedOutputStream to which the response is written.
     * @throws IOException If an I/O error occurs during writing.
     */
    void write(final Response response, final BufferedOutputStream boss) throws IOException;

}
