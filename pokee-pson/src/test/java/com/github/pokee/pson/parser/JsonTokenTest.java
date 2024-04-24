package com.github.pokee.pson.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTokenTest {

    @Test
    void testResetPosition() {
        final JsonTokenizer tokenizer = new JsonTokenizer("{\"key\":\"value\"}");
        assertEquals(0, tokenizer.getIndex());

        JsonToken token = tokenizer.nextToken();
        assertEquals(JsonTokenType.BEGIN_OBJECT, token.type());
        assertEquals(1, tokenizer.getIndex());

        token.resetPosition(tokenizer);
        assertEquals(0, tokenizer.getIndex());
        token = tokenizer.nextToken();
        assertEquals(JsonTokenType.BEGIN_OBJECT, token.type());
    }

    @Test
    void testApply() {
        final JsonTokenizer tokenizer = new JsonTokenizer("{\"key\":\"value\"}");
        assertEquals(0, tokenizer.getIndex());

        JsonToken token = tokenizer.peekNextToken();
        assertEquals(JsonTokenType.BEGIN_OBJECT, token.type());
        assertEquals(0, tokenizer.getIndex());

        token = tokenizer.peekNextToken();
        assertEquals(JsonTokenType.BEGIN_OBJECT, token.type());
        assertEquals(0, tokenizer.getIndex());

        token.apply(tokenizer);

        token = tokenizer.peekNextToken();
        assertEquals(JsonTokenType.STRING, token.type());
        assertEquals(1, tokenizer.getIndex());
    }

}