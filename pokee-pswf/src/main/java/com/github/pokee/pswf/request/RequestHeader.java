package com.github.pokee.pswf.request;

/**
 * Represents the initial line of an HTTP request, containing the method, path, and version.
 */
public record RequestHeader(Method method, String path, Version version) {

    /**
     * Parses a request line string into a {@link RequestHeader} object.
     * The request line should contain exactly three components: the method, the path, and the version.
     *
     * @param line The request line string to be parsed.
     * @return A {@link RequestHeader} object representing the parsed request line.
     * @throws IllegalArgumentException If the request line is malformed or contains invalid components.
     */
    public static RequestHeader parseLine(final String line) {
        final String[] split = line.trim().split(" ");
        if (split.length != 3) {
            throw new IllegalArgumentException("Invalid request line: " + line);
        }
        final Method method = Method.fromString(split[0]);
        if (method == null) {
            throw new IllegalArgumentException("Invalid method: " + split[0]);
        }
        final Version version = Version.fromString(split[2]);
        if (version == null) {
            throw new IllegalArgumentException("Invalid version: " + split[2]);
        }
        return new RequestHeader(method, split[1], version);
    }

    /**
     * Checks if the request header is valid by ensuring that the method, path, and version are not null,
     * and that the path is not empty.
     *
     * @return true if the request header is valid, otherwise false.
     */
    public boolean isValid() {
        return this.method != null && this.path != null && this.version != null && !this.path.isEmpty();
    }

}
