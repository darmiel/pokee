package com.github.pokee.psql;

import com.github.pokee.psql.domain.token.Token;
import com.github.pokee.psql.domain.token.support.TokenType;
import com.github.pokee.psql.exception.LexerException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LexerTest {

    @Test
    void testInitialization() {
        final String query = "use Pokemon as P; query all P::*;";
        final Lexer lexer = new Lexer(query);
        assertEquals(query, lexer.getQuery(), "Initial query string should match input.");
        assertEquals(0, lexer.getCurrentIndex(), "Initial index should be 0.");
        assertNull(lexer.getCurrentToken(), "No token should be processed initially.");
    }

    private TokenWithValue withValue(final TokenType type, final String value) {
        return new TokenWithValue(type, value);
    }

    private void testAll(final Lexer lexer, final Object... tests) {
        for (final Object test : tests) {
            lexer.nextToken();
            final Token current = lexer.getCurrentToken();

            if (test instanceof final TokenWithValue tokenWithValue) {
                assertNotNull(current, "Token should not be null.");
                assertEquals(tokenWithValue.type(), current.type(), "Token type should match.");
                assertEquals(tokenWithValue.value(), current.value(), "Token value should match.");
            } else if (test instanceof final TokenType tokenType) {
                assertNotNull(current, "Token should not be null.");
                assertEquals(tokenType, current.type(), "Token type should match.");
            } else {
                fail("Unknown test type: " + test.getClass());
            }
        }
    }

    @Test
    void testKBigQuery() {
        final Lexer lexer = new Lexer("""
                USE Pokemon;
                USE Pokemon AS P;
                                
                QUERY all Pokemon::{
                	id AS poke_id,
                	name
                } FILTER Pokemon::id.gt(0)""");
        this.testAll(lexer,
                TokenType.USE, withValue(TokenType.IDENTIFIER, "Pokemon"), TokenType.SEMICOLON,
                TokenType.USE, withValue(TokenType.IDENTIFIER, "Pokemon"), TokenType.AS, withValue(TokenType.IDENTIFIER, "P"), TokenType.SEMICOLON,
                TokenType.QUERY, withValue(TokenType.IDENTIFIER, "all"), withValue(TokenType.NAMESPACE_NAME, "Pokemon"), TokenType.LBRACE,
                withValue(TokenType.IDENTIFIER, "id"), TokenType.AS, withValue(TokenType.IDENTIFIER, "poke_id"), TokenType.COMMA,
                withValue(TokenType.IDENTIFIER, "name"),
                TokenType.RBRACE, TokenType.FILTER, TokenType.NAMESPACE_NAME, withValue(TokenType.IDENTIFIER, "id"), TokenType.DOT, withValue(TokenType.FUNCTION_NAME, "gt"),
                TokenType.LPAREN, withValue(TokenType.NUMBER, "0"), TokenType.RPAREN, TokenType.EOF);
    }

    @Test
    void testSimpleChars() {
        final Lexer lexer = new Lexer("""
                ()[]{}*,.;""");
        this.testAll(lexer,
                TokenType.LPAREN, TokenType.RPAREN,
                TokenType.LBRACKET, TokenType.RBRACKET,
                TokenType.LBRACE, TokenType.RBRACE,
                TokenType.STAR,
                TokenType.COMMA,
                TokenType.DOT,
                TokenType.SEMICOLON,
                TokenType.EOF
        );
    }

    @Test
    void testOperators() {
        final Lexer lexer = new Lexer("""
                AND &&
                OR ||""");
        this.testAll(lexer,
                TokenType.AND, TokenType.AND,
                TokenType.OR, TokenType.OR,
                TokenType.EOF
        );
    }

    @Test
    void testWhitespace() {
        assertFalse(new Lexer("").nextToken(), "No token should be processed from empty input.");
        assertFalse(new Lexer("    ").nextToken(), "No token should be processed from only whitespaces.");
    }

    @Test
    void testUnrecognizedCharacterException() {
        final Lexer lexer = new Lexer("?");
        assertThrows(LexerException.class, lexer::nextToken, "Should throw LexerException for unrecognized characters.");
    }

    @Test
    void testUnrecognizedOperator() {
        assertThrows(LexerException.class, () -> new Lexer("=|").nextToken(),
                "Should throw LexerException for unrecognized operator.");

        assertThrows(LexerException.class, () -> new Lexer("A:T").nextToken(),
                "Should throw LexerException for invalid namespace");
    }

    @Test
    void testReset() {
        final Lexer lexer = new Lexer("A; B");
        lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, lexer.getCurrentToken().type());
        assertEquals("A", lexer.getCurrentToken().value());
        assertEquals(1, lexer.getCurrentIndex(), "Index should be 1.");

        lexer.nextToken();
        assertEquals(TokenType.SEMICOLON, lexer.getCurrentToken().type());
        lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, lexer.getCurrentToken().type());
        assertEquals("B", lexer.getCurrentToken().value());

        lexer.reset();
        lexer.nextToken();
        assertEquals(TokenType.IDENTIFIER, lexer.getCurrentToken().type());
        assertEquals("A", lexer.getCurrentToken().value());
        assertEquals(1, lexer.getCurrentIndex(), "Index should be 1.");
    }

    record TokenWithValue(TokenType type, String value) {
    }


}