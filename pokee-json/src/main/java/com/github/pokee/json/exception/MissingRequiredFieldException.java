package com.github.pokee.json.exception;

public class MissingRequiredFieldException extends Exception {

    private final String key;

    public MissingRequiredFieldException(final String key) {
        this.key = key;
    }

    @Override
    public String getMessage() {
        return String.format("Missing required field: %s. Annotate with @Optional if nullable", this.key);
    }

}
