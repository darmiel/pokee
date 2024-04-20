package com.github.pokee.stick.request;

import com.github.pokee.stick.request.parsers.RequestParser;
import com.github.pokee.stick.request.parsers.RequestParserVersion1_0;
import com.github.pokee.stick.request.parsers.RequestParserVersion1_1;
import com.github.pokee.stick.response.writers.ResponseWriter;
import com.github.pokee.stick.response.writers.ResponseWriterVersion1_0;
import com.github.pokee.stick.response.writers.ResponseWriterVersion1_1;

/**
 * Defines the versions of the HTTP protocol supported, along with corresponding request parsers
 * and response writers.
 */
public enum Version {

    VERSION_0_9("HTTP/0.9", null, null),
    VERSION_1_0("HTTP/1.0", new RequestParserVersion1_0(), new ResponseWriterVersion1_0()),
    VERSION_1_1("HTTP/1.1", new RequestParserVersion1_1(), new ResponseWriterVersion1_1()),
    VERSION_2_0("HTTP/2.0", null, null),
    VERSION_3_0("HTTP/3.0", null, null);

    private final String version;
    private final RequestParser parser;
    private final ResponseWriter writer;


    /**
     * Constructs a version of the HTTP protocol with the specified request parser and response writer.
     *
     * @param version the string representation of the HTTP version
     * @param parser  the parser for processing incoming requests for this version
     * @param writer  the writer for crafting responses in this version
     */
    Version(final String version, final RequestParser parser, final ResponseWriter writer) {
        this.version = version;
        this.parser = parser;
        this.writer = writer;
    }

    /**
     * Returns the enum constant of this type with the specified name.
     * The string must match exactly an identifier used to declare an enum constant in this type.
     *
     * @param versionString the name of the requested version
     * @return the Version constant, or null if the version is not recognized
     */
    public static Version fromString(final String versionString) {
        for (final Version version : values()) {
            if (version.version.equalsIgnoreCase(versionString)) {
                return version;
            }
        }
        return null;
    }

    /**
     * Returns the parser associated with this HTTP version.
     *
     * @return the request parser, or null if none is associated
     */
    public RequestParser getParser() {
        return this.parser;
    }

    /**
     * Returns the writer associated with this HTTP version.
     *
     * @return the response writer, or null if none is associated
     */
    public ResponseWriter getWriter() {
        return this.writer;
    }

    /**
     * Returns the string representation of this HTTP version.
     *
     * @return the string representation of the version
     */
    @Override
    public String toString() {
        return this.version;
    }

}
