package com.github.pokee.stick.response.writers;

import com.github.pokee.stick.Version;

/**
 * A response writer for HTTP/1.1 responses. It uses the HTTP/1.0 specification to format and output HTTP responses
 * but changes the version to 1.1. They are nearly the same anyway :)
 */
public class ResponseWriterVersion1_1 extends ResponseWriterVersion1_0 {

    /**
     * {@inheritDoc}
     */
    @Override
    protected Version getVersion() {
        return Version.VERSION_1_1;
    }

}
