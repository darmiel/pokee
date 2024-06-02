package com.github.pokee.psql.domain.token.support;

public enum TokenType {

    // value tokens
    IDENTIFIER(TokenKind.IDENTIFIER),
    FUNCTION_NAME(TokenKind.IDENTIFIER),
    NAMESPACE_NAME(TokenKind.IDENTIFIER),
    USE(TokenKind.KEYWORD),
    AS(TokenKind.KEYWORD),
    STRING_LITERAL(TokenKind.LITERAL),
    NUMBER(TokenKind.LITERAL),

    // simple symbols
    LPAREN(TokenKind.PUNCTUATION),
    RPAREN(TokenKind.PUNCTUATION),
    LBRACKET(TokenKind.PUNCTUATION),
    RBRACKET(TokenKind.PUNCTUATION),
    LBRACE(TokenKind.PUNCTUATION),
    RBRACE(TokenKind.PUNCTUATION),
    COMMA(TokenKind.PUNCTUATION),
    DOT(TokenKind.PUNCTUATION),
    SEMICOLON(TokenKind.PUNCTUATION),

    // other keywords
    STAR(TokenKind.KEYWORD),
    NOT(TokenKind.KEYWORD),
    AND(TokenKind.KEYWORD),
    OR(TokenKind.KEYWORD),
    FILTER(TokenKind.KEYWORD),
    MAP(TokenKind.KEYWORD),
    QUERY(TokenKind.KEYWORD),
    LANGUAGE(TokenKind.KEYWORD),
    EOF(TokenKind.EOF);

    private final TokenKind type;

    TokenType(final TokenKind type) {
        this.type = type;
    }

    public TokenKind getTokenTypeType() {
        return this.type;
    }

    public boolean isLiteral() {
        return this.type == TokenKind.LITERAL;
    }

    public boolean isIdentifier() {
        return this.type == TokenKind.IDENTIFIER;
    }

    public boolean isKeyword() {
        return this.type == TokenKind.KEYWORD;
    }

    public boolean isPunctuation() {
        return this.type == TokenKind.PUNCTUATION;
    }

}
