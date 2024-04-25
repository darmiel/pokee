package com.github.pokee.psql.domain.token.support;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TokenTypeTest {

    @Test
    void getTokenTypeType() {
        assertEquals(TokenTypeType.KEYWORD, TokenType.AS.getTokenTypeType());
    }

    @Test
    void isLiteral() {
        assertTrue(TokenType.STRING_LITERAL.isLiteral());
        assertTrue(TokenType.NUMBER.isLiteral());
    }

    @Test
    void isIdentifier() {
        assertTrue(TokenType.IDENTIFIER.isIdentifier());
        assertTrue(TokenType.FUNCTION_NAME.isIdentifier());
        assertTrue(TokenType.NAMESPACE_NAME.isIdentifier());
    }

    @Test
    void isKeyword() {
        assertTrue(TokenType.USE.isKeyword());
        assertTrue(TokenType.AS.isKeyword());
        assertTrue(TokenType.FILTER.isKeyword());
        assertTrue(TokenType.MAP.isKeyword());
        assertTrue(TokenType.QUERY.isKeyword());
        assertTrue(TokenType.LANGUAGE.isKeyword());
        assertTrue(TokenType.STAR.isKeyword());
    }

    @Test
    void isPunctuation() {
        assertTrue(TokenType.LPAREN.isPunctuation());
        assertTrue(TokenType.RPAREN.isPunctuation());
        assertTrue(TokenType.LBRACKET.isPunctuation());
        assertTrue(TokenType.RBRACKET.isPunctuation());
        assertTrue(TokenType.LBRACE.isPunctuation());
        assertTrue(TokenType.RBRACE.isPunctuation());
        assertTrue(TokenType.COMMA.isPunctuation());
        assertTrue(TokenType.DOT.isPunctuation());
        assertTrue(TokenType.SEMICOLON.isPunctuation());
    }
}