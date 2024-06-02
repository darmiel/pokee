package com.github.pokee.psql.domain.token.support;

public enum TokenKind {

    KEYWORD,
    IDENTIFIER,
    LITERAL,
    OPERATOR,
    PUNCTUATION,
    EOF;

    public static TokenKind fromString(final String type) {
        return TokenKind.valueOf(type.toUpperCase());
    }

}
