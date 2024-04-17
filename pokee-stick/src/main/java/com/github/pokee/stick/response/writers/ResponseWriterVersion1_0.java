package com.github.pokee.stick.response.writers;

import com.github.pokee.stick.Version;
import com.github.pokee.stick.response.Response;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * A response writer for HTTP/1.0 responses. This class handles the formatting
 * and output of HTTP responses according to the HTTP/1.0 specification.
 */
public class ResponseWriterVersion1_0 implements ResponseWriter {

    /**
     * Returns the delimiter used to separate headers and lines in the HTTP response.
     *
     * @return The delimiter string.
     */
    protected String getDelimiter() {
        return "\r\n";
    }

    /**
     * Returns the HTTP version this writer handles.
     *
     * @return The HTTP version.
     */
    protected Version getVersion() {
        return Version.VERSION_1_0;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void write(final Response response, final BufferedOutputStream boss) throws IOException {
        final StringBuilder bob = new StringBuilder();

        // write header
        bob.append(this.getVersion()).append(" ").append(response.statusCode());
        if (response.statusMessage() != null && !response.statusMessage().isBlank()) {
            bob.append(" ").append(response.statusMessage());
        }
        bob.append(this.getDelimiter());

        // write response headers
        for (final Map.Entry<String, List<String>> entry : response.headers().entries()) {
            for (final String value : entry.getValue()) {
                bob.append(entry.getKey()).append(": ").append(value).append(this.getDelimiter());
            }
        }

        // write body
        bob.append(this.getDelimiter());
        final byte[] headerBytes = bob.toString().getBytes();

        final byte[] totalBytes;
        if (response.body().length > 0) {
            final int totalContentLength = headerBytes.length + response.body().length;
            totalBytes = new byte[totalContentLength];
            System.arraycopy(headerBytes, 0, totalBytes, 0, headerBytes.length);
            System.arraycopy(response.body(), 0, totalBytes, headerBytes.length, response.body().length);
        } else {
            totalBytes = headerBytes;
        }

        boss.write(totalBytes);
    }

}
