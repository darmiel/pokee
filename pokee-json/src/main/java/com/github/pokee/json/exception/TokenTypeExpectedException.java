package com.github.pokee.json.exception;

import com.github.pokee.json.token.JsonTokenType;

import java.util.Arrays;

/**
 * Exception thrown when a token type is expected but another token type is found
 */
public class TokenTypeExpectedException extends Exception {

    private final JsonTokenType[] expected;
    private final JsonTokenType actual;

    public TokenTypeExpectedException(final JsonTokenType[] expected, final JsonTokenType actual) {
        this.expected = expected;
        this.actual = actual;
    }

    public TokenTypeExpectedException(final JsonTokenType expected, final JsonTokenType actual) {
        this.expected = new JsonTokenType[]{expected};
        this.actual = actual;
    }

    @Override
    public String getMessage() {
        return String.format("Expected one of %s but got %s", Arrays.toString(this.expected), this.actual);
    }

}
