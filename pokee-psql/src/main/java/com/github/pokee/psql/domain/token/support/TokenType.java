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
    COMMA(TokenTypeType.PUNCTUATION),
    DOT(TokenTypeType.PUNCTUATION),
    SEMICOLON(TokenTypeType.PUNCTUATION),

    // other keywords
    STAR(TokenTypeType.KEYWORD),
    NOT(TokenTypeType.KEYWORD),
    AND(TokenTypeType.KEYWORD),
    OR(TokenTypeType.KEYWORD),
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

    public boolean isPunctuation() {
        return this.type == TokenTypeType.PUNCTUATION;
    }

}
