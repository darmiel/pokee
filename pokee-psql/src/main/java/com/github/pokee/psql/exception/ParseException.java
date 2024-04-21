package com.github.pokee.psql.exception;

import com.github.pokee.psql.domain.token.Token;

public class ParseException extends Exception {

    private final int index;
    private final Token currentToken;

    private final String formattedError;
    private final String message;

    public ParseException(final int index, final Token currentToken, final String formattedError, final String message) {
        this.index = index;
        this.currentToken = currentToken;

        this.formattedError = formattedError;
        this.message = message;
    }

    public ParseException(final int index, final Token currentToken, final String formattedError) {
        this(index, currentToken, formattedError, "Error parsing query");
    }

    public int getIndex() {
        return index;
    }

    public Token getCurrentToken() {
        return currentToken;
    }

    public String getFormattedError() {
        return formattedError;
    }

    @Override
    public String getMessage() {
        return this.message + "\n" + this.formattedError;
    }

}
