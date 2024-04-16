package com.github.pokee.stick.exception;

import java.io.IOException;

public class RequestParseException extends IOException {

    public RequestParseException(final String message) {
        super(message);
    }

}
