package com.github.pokee.psql.domain.token.support;

public enum TokenType {

    // value tokens
    IDENTIFIER(TokenTypeType.IDENTIFIER),
    FUNCTION_NAME(TokenTypeType.IDENTIFIER),
    NAMESPACE_NAME(TokenTypeType.IDENTIFIER),
    USE(TokenTypeType.KEYWORD),
    AS(TokenTypeType.KEYWORD),
    STRING_LITERAL(TokenTypeType.LITERAL),
    NUMBER(TokenTypeType.LITERAL),

    // simple symbols
    LPAREN(TokenTypeType.PUNCTUATION),
    RPAREN(TokenTypeType.PUNCTUATION),
    LBRACKET(TokenTypeType.PUNCTUATION),
    RBRACKET(TokenTypeType.PUNCTUATION),
    LBRACE(TokenTypeType.PUNCTUATION),
    RBRACE(TokenTypeType.PUNCTUATION),
    STAR(TokenTypeType.OPERATOR),
    COMMA(TokenTypeType.PUNCTUATION),
    DOT(TokenTypeType.PUNCTUATION),
    SEMICOLON(TokenTypeType.PUNCTUATION),

    // logic operators
    BOOL_TRUE(TokenTypeType.LITERAL),
    BOOL_FALSE(TokenTypeType.LITERAL),
    BOOL_AND(TokenTypeType.OPERATOR),
    BOOL_OR(TokenTypeType.OPERATOR),

    // math operators
    BIT_AND(TokenTypeType.OPERATOR),
    BIT_OR(TokenTypeType.OPERATOR),
    BIT_XOR(TokenTypeType.OPERATOR),
    BIT_NOT(TokenTypeType.OPERATOR),
    BIT_SHIFT_LEFT(TokenTypeType.OPERATOR),
    BIT_SHIFT_RIGHT(TokenTypeType.OPERATOR),
    PLUS(TokenTypeType.OPERATOR),
    MINUS(TokenTypeType.OPERATOR),
    DIVIDE(TokenTypeType.OPERATOR),
    MODULO(TokenTypeType.OPERATOR),

    // comparison operators
    CMP_EQUALS(TokenTypeType.COMPARE_OPERATOR),
    CMP_NOT_EQUALS(TokenTypeType.COMPARE_OPERATOR),
    CMP_LESS_THAN(TokenTypeType.COMPARE_OPERATOR),
    CMP_LESS_OR_EQUALS(TokenTypeType.COMPARE_OPERATOR),
    CMP_GREATER_THAN(TokenTypeType.COMPARE_OPERATOR),
    CMP_GREATER_OR_EQUALS(TokenTypeType.COMPARE_OPERATOR),

    // other keywords
    FILTER(TokenTypeType.KEYWORD),
    MAP(TokenTypeType.KEYWORD),
    QUERY(TokenTypeType.KEYWORD),
    LANGUAGE(TokenTypeType.KEYWORD),
    EOF(TokenTypeType.EOF);

    private final TokenTypeType type;

    TokenType(final TokenTypeType type) {
        this.type = type;
    }

    public TokenTypeType getTokenTypeType() {
        return this.type;
    }

    public boolean isLiteral() {
        return this.type == TokenTypeType.LITERAL;
    }

    public boolean isIdentifier() {
        return this.type == TokenTypeType.IDENTIFIER;
    }

    public boolean isKeyword() {
        return this.type == TokenTypeType.KEYWORD;
    }

    public boolean isOperator() {
        return this.type == TokenTypeType.OPERATOR;
    }

    public boolean isCompareOperator() {
        return this.type == TokenTypeType.COMPARE_OPERATOR;
    }

    public boolean isPunctuation() {
        return this.type == TokenTypeType.PUNCTUATION;
    }

    public boolean isWhitespace() {
        return this.type == TokenTypeType.WHITESPACE;
    }

    public boolean isComment() {
        return type == TokenTypeType.COMMENT;
    }

}
