package com.github.pokee.psql.domain.token.support;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TokenTypeTypeTest {

    @Test
    void fromString() {
        assertEquals(TokenTypeType.KEYWORD, TokenTypeType.fromString("KEYWORD"));
        assertEquals(TokenTypeType.IDENTIFIER, TokenTypeType.fromString("IDENTIFIER"));
        assertEquals(TokenTypeType.LITERAL, TokenTypeType.fromString("LITERAL"));
        assertEquals(TokenTypeType.OPERATOR, TokenTypeType.fromString("OPERATOR"));
        assertEquals(TokenTypeType.COMPARE_OPERATOR, TokenTypeType.fromString("COMPARE_OPERATOR"));
        assertEquals(TokenTypeType.PUNCTUATION, TokenTypeType.fromString("PUNCTUATION"));
        assertEquals(TokenTypeType.EOF, TokenTypeType.fromString("EOF"));
    }

    @Test
    void fromStringInvalid() {
        assertThrows(IllegalArgumentException.class, () -> TokenTypeType.fromString("INVALID"));
    }

}