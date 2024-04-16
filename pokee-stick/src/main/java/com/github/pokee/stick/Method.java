package com.github.pokee.stick;

import com.github.pokee.stick.util.Enums;

// currently, we only support GET, POST, PUT, DELETE, PATCH
// but this is enough for this project.
public enum Method {
    GET,
    POST,
    PUT,
    DELETE,
    PATCH;

    public static Method fromString(final String method) {
        return Enums.fromString(Method.class, method);
    }
}
