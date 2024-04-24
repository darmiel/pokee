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
        assertTrue(TokenType.BOOL_TRUE.isLiteral());
        assertTrue(TokenType.BOOL_FALSE.isLiteral());
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
    }

    @Test
    void isOperator() {
        assertTrue(TokenType.STAR.isOperator());
        assertTrue(TokenType.BOOL_AND.isOperator());
        assertTrue(TokenType.BOOL_OR.isOperator());
        assertTrue(TokenType.BIT_AND.isOperator());
        assertTrue(TokenType.BIT_OR.isOperator());
        assertTrue(TokenType.BIT_XOR.isOperator());
        assertTrue(TokenType.BIT_NOT.isOperator());
        assertTrue(TokenType.BIT_SHIFT_LEFT.isOperator());
        assertTrue(TokenType.BIT_SHIFT_RIGHT.isOperator());
        assertTrue(TokenType.PLUS.isOperator());
        assertTrue(TokenType.MINUS.isOperator());
        assertTrue(TokenType.DIVIDE.isOperator());
        assertTrue(TokenType.MODULO.isOperator());
    }

    @Test
    void isCompareOperator() {
        assertTrue(TokenType.CMP_EQUALS.isCompareOperator());
        assertTrue(TokenType.CMP_NOT_EQUALS.isCompareOperator());
        assertTrue(TokenType.CMP_LESS_THAN.isCompareOperator());
        assertTrue(TokenType.CMP_LESS_OR_EQUALS.isCompareOperator());
        assertTrue(TokenType.CMP_GREATER_THAN.isCompareOperator());
        assertTrue(TokenType.CMP_GREATER_OR_EQUALS.isCompareOperator());
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