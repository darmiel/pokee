package com.github.pokee.stick.request;

import com.github.pokee.stick.Method;
import com.github.pokee.stick.Version;

public record RequestHeader(Method method, String path, Version version) {

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

    public boolean isValid() {
        return this.method != null && this.path != null && this.version != null && !this.path.isEmpty();
    }

}
