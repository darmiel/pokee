package com.github.pokee.psql.domain.token.support;

public enum TokenTypeType {

    KEYWORD,
    IDENTIFIER,
    LITERAL,
    OPERATOR,
    COMPARE_OPERATOR,
    PUNCTUATION,
    WHITESPACE,
    COMMENT,
    EOF;

    public static TokenTypeType fromString(final String type) {
        return TokenTypeType.valueOf(type.toUpperCase());
    }

}
