package com.github.pokee.stick.exception.request;

public class NoContentException extends RequestException {

    public NoContentException() {
        super("No content provided for the response");
    }

}
