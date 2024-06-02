package com.github.pokee.psql.domain.token.support;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TokenKindTest {

    @Test
    void fromString() {
        assertEquals(TokenKind.KEYWORD, TokenKind.fromString("KEYWORD"));
        assertEquals(TokenKind.IDENTIFIER, TokenKind.fromString("IDENTIFIER"));
        assertEquals(TokenKind.LITERAL, TokenKind.fromString("LITERAL"));
        assertEquals(TokenKind.OPERATOR, TokenKind.fromString("OPERATOR"));
        assertEquals(TokenKind.PUNCTUATION, TokenKind.fromString("PUNCTUATION"));
        assertEquals(TokenKind.EOF, TokenKind.fromString("EOF"));
    }

    @Test
    void fromStringInvalid() {
        assertThrows(IllegalArgumentException.class, () -> TokenKind.fromString("INVALID"));
    }

}