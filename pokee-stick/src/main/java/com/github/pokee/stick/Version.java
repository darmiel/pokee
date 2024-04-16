package com.github.pokee.stick;

import com.github.pokee.stick.request.parsers.RequestParser;
import com.github.pokee.stick.request.parsers.RequestParserVersion1_0;
import com.github.pokee.stick.request.parsers.RequestParserVersion1_1;
import com.github.pokee.stick.response.writers.ResponseWriter;
import com.github.pokee.stick.response.writers.ResponseWriterVersion1_0;
import com.github.pokee.stick.response.writers.ResponseWriterVersion1_1;

public enum Version {

    VERSION_0_9("HTTP/0.9", null, null),
    VERSION_1_0("HTTP/1.0", new RequestParserVersion1_0(), new ResponseWriterVersion1_0()),
    VERSION_1_1("HTTP/1.1", new RequestParserVersion1_1(), new ResponseWriterVersion1_1()),
    VERSION_2_0("HTTP/2.0", null, null),
    VERSION_3_0("HTTP/3.0", null, null);

    private final String version;
    private final RequestParser parser;
    private final ResponseWriter writer;

    Version(final String version, final RequestParser parser, final ResponseWriter writer) {
        this.version = version;
        this.parser = parser;
        this.writer = writer;
    }

    public RequestParser getParser() {
        return parser;
    }

    public ResponseWriter getWriter() {
        return writer;
    }

    public static Version fromString(final String versionString) {
        for (final Version version : values()) {
            if (version.version.equalsIgnoreCase(versionString)) {
                return version;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.version;
    }

}
