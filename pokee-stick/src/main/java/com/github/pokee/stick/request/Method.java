package com.github.pokee.stick.request;

import com.github.pokee.stick.util.Enums;

// currently, we only support GET, POST, PUT, DELETE, PATCH
// but this is enough for this project.
public enum Method {
    GET,
    POST,
    PUT,
    DELETE,
    PATCH;

    /**
     * Returns the enum constant of this type with the specified name.
     *
     * @param method the name of the enum constant to be returned.
     * @return the enum constant with the specified name or null if not found.
     */
    public static Method fromString(final String method) {
        return Enums.fromString(Method.class, method);
    }

}
