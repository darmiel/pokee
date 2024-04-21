package com.github.pokee.psql.domain.token.support;

public enum TokenType {

    // value tokens
    IDENTIFIER,
    QUERY_NAME,
    FUNCTION_NAME,
    NAMESPACE_NAME,
    USE,
    AS,
    STRING_LITERAL,
    NUMBER,

    // simple symbols
    LPAREN, //
    RPAREN, //
    LBRACKET, //
    RBRACKET, //
    LBRACE, //
    RBRACE, //
    STAR, //
    COMMA, //
    DOT, //
    SEMICOLON, //

    // logic operators
    BOOL_AND, //
    BOOL_OR, //

    // math operators
    BIT_AND, //
    BIT_OR, //
    BIT_XOR, //
    BIT_NOT, //
    BIT_SHIFT_LEFT, //
    BIT_SHIFT_RIGHT, //
    PLUS,
    MINUS,
    DIVIDE,
    MODULO,

    // arrows
    LEFT_ARROW,
    RIGHT_ARROW,
    LEFT_RIGHT_ARROW,

    // comparison operators
    CMP_EQUALS, //
    CMP_NOT_EQUALS, //
    CMP_LOWER_THAN, //
    CMP_LOWER_OR_EQUALS, //
    CMP_GREATER_THAN, //
    CMP_GREATER_OR_EQUALS, //

    FILTER,
    MAP,
    EOF,

}
