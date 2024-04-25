package com.github.pokee.pswf.request.parsers;

import com.github.pokee.pswf.headers.Headers;
import com.github.pokee.pswf.request.Request;
import com.github.pokee.pswf.request.RequestHeader;
import com.github.pokee.pswf.util.UrlSearchParams;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Implements a request parser for HTTP/1.0. This parser reads the request line and headers,
 * constructing a {@link Request} object according to HTTP/1.0 specifications.
 */
public class RequestParserVersion1_0 implements RequestParser {

    /**
     * Parses an HTTP request using a {@link BufferedReader} to read the request header and body.
     * It builds a {@link Request} object containing method, path, query parameters, version,
     * headers, and the reader itself for further body reading.
     *
     * @param header The {@link RequestHeader} object containing the initial request line information.
     * @param reader A {@link BufferedReader} from which the request headers and body are read.
     * @return A {@link Request} object populated with parsed values from the HTTP request.
     * @throws IOException If an I/O error occurs during reading from the reader.
     */
    @Override
    public Request parse(final RequestHeader header, final BufferedReader reader) throws IOException {
        final Headers headers = new Headers();
        while (true) {
            final String line = reader.readLine();
            if (line == null || line.isBlank()) {
                break;
            }
            final int colonIndex = line.indexOf(':');

            final String key = line.substring(0, colonIndex);
            final String value = line.substring(colonIndex + 1).trim();
            headers.add(key, value);
        }

        final String unescapedPath = UrlSearchParams.unescape(header.path());

        final String path;
        final String query;
        if (unescapedPath.contains("?")) {
            final int index = unescapedPath.indexOf("?");
            path = unescapedPath.substring(0, index);
            query = unescapedPath.substring(index + 1);
        } else {
            path = unescapedPath;
            query = "";
        }

        return new Request(
                header.method(),
                path,
                query,
                header.version(),
                headers.toImmutable(),
                reader
        );
    }

}
