package com.github.pokee.json.token;

import junit.framework.TestCase;

public class JsonTokenTest extends TestCase {

    public void testResetPosition() {
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

    public void testApply() {
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