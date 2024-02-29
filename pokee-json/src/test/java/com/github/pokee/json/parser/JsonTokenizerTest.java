package com.github.pokee.json.parser;

import junit.framework.TestCase;

public class JsonTokenizerTest extends TestCase {

    public static final String JSON = "{\"key\":\"value\"}";

    private void assertTokenTypes(final JsonTokenizer tokenizer, final JsonTokenType... types) {
        for (final JsonTokenType type : types) {
            assertEquals(type, tokenizer.nextToken().type());
        }
    }

    public void testAdvanceTo() {
        final JsonTokenizer tokenizer = new JsonTokenizer(JSON);
        assertEquals(0, tokenizer.getIndex());
        tokenizer.advanceTo(5);
        assertEquals(5, tokenizer.getIndex());
    }

    public void testPeekNextToken() {
        final JsonTokenizer tokenizer = new JsonTokenizer(JSON);
        assertEquals(0, tokenizer.getIndex());
        final JsonToken token = tokenizer.peekNextToken();
        assertEquals(0, tokenizer.getIndex());
        assertEquals(JsonTokenType.BEGIN_OBJECT, token.type());
        assertEquals("{", token.value());
    }

    public void testNextToken() {
        final JsonTokenizer tokenizer = new JsonTokenizer(JSON);
        assertEquals(JsonTokenType.BEGIN_OBJECT, tokenizer.nextToken().type());
        assertEquals(JsonTokenType.STRING, tokenizer.nextToken().type());
        assertEquals(JsonTokenType.NAME_SEPARATOR, tokenizer.nextToken().type());
        assertEquals(JsonTokenType.STRING, tokenizer.nextToken().type());
        assertEquals(JsonTokenType.END_OBJECT, tokenizer.nextToken().type());
        assertEquals(JsonTokenType.END_DOCUMENT, tokenizer.nextToken().type());
    }

    public void testSkipWhitespace() {
        final JsonTokenizer tokenizer = new JsonTokenizer("{    \"key\"    :    \"value\"    }");
        assertEquals(JsonTokenType.BEGIN_OBJECT, tokenizer.nextToken().type());

        JsonToken token = tokenizer.nextToken();
        assertEquals(JsonTokenType.STRING, token.type());
        assertEquals("\"key\"", token.value());

        assertEquals(JsonTokenType.NAME_SEPARATOR, tokenizer.nextToken().type());

        token = tokenizer.nextToken();
        assertEquals(JsonTokenType.STRING, token.type());
        assertEquals("\"value\"", token.value());

        assertEquals(JsonTokenType.END_OBJECT, tokenizer.nextToken().type());
        assertEquals(JsonTokenType.END_DOCUMENT, tokenizer.nextToken().type());
    }

    public void testTypes() {
        final JsonTokenizer tokenizer = new JsonTokenizer("""
                {
                    "object": {},
                    "array": [],
                    "string": "string",
                    "number": 10,
                    "boolean": true,
                    "null": null
                }
                """);
        this.assertTokenTypes(tokenizer,
                JsonTokenType.BEGIN_OBJECT,
                JsonTokenType.STRING, JsonTokenType.NAME_SEPARATOR, JsonTokenType.BEGIN_OBJECT, JsonTokenType.END_OBJECT, JsonTokenType.VALUE_SEPARATOR,
                JsonTokenType.STRING, JsonTokenType.NAME_SEPARATOR, JsonTokenType.BEGIN_ARRAY, JsonTokenType.END_ARRAY, JsonTokenType.VALUE_SEPARATOR,
                JsonTokenType.STRING, JsonTokenType.NAME_SEPARATOR, JsonTokenType.STRING, JsonTokenType.VALUE_SEPARATOR,
                JsonTokenType.STRING, JsonTokenType.NAME_SEPARATOR, JsonTokenType.NUMBER, JsonTokenType.VALUE_SEPARATOR,
                JsonTokenType.STRING, JsonTokenType.NAME_SEPARATOR, JsonTokenType.BOOLEAN, JsonTokenType.VALUE_SEPARATOR,
                JsonTokenType.STRING, JsonTokenType.NAME_SEPARATOR, JsonTokenType.NULL,
                JsonTokenType.END_OBJECT, JsonTokenType.END_DOCUMENT);
    }


    public void testUnexpectedCharacter() {
        final JsonTokenizer tokenizer = new JsonTokenizer("a");
        try {
            tokenizer.nextToken();
            fail("Expected an exception");
        } catch (final IllegalStateException e) {
            assertEquals("Unexpected character: a", e.getMessage());
        }
    }

    public void testReadString() {
        final JsonTokenizer tokenizer = new JsonTokenizer(JSON);
        tokenizer.nextToken();
        assertEquals("\"key\"", tokenizer.readString());
        tokenizer.nextToken();
        assertEquals("\"value\"", tokenizer.readString());
    }

    public void testUnterminatedString() {
        final JsonTokenizer tokenizer = new JsonTokenizer("{\"key\":\"value");
        this.assertTokenTypes(tokenizer, JsonTokenType.BEGIN_OBJECT, JsonTokenType.STRING, JsonTokenType.NAME_SEPARATOR);
        try {
            tokenizer.readString();
            fail("Expected an exception");
        } catch (final IllegalStateException e) {
            assertEquals("Unterminated string", e.getMessage());
        }
    }

    public void testReadPrimitive() {
        final JsonTokenizer tokenizer = new JsonTokenizer("{\"key\":true,\"key2\":10.5,\"key3\":null}");
        tokenizer.nextToken();
        tokenizer.nextToken();
        tokenizer.nextToken();
        assertEquals("true", tokenizer.readPrimitive());
        tokenizer.nextToken();
        tokenizer.nextToken();
        tokenizer.nextToken();
        assertEquals("10.5", tokenizer.readPrimitive());
        tokenizer.nextToken();
        tokenizer.nextToken();
        tokenizer.nextToken();
        assertEquals("null", tokenizer.readPrimitive());
    }

    public void testInvalidPrimitives() {
        final JsonTokenizer tokenizer = new JsonTokenizer("{\"true\":true,\"null\":null,\"num\":10,\"truee\":truee,\"nul\":nul}");
        this.assertTokenTypes(tokenizer, JsonTokenType.BEGIN_OBJECT, JsonTokenType.STRING, JsonTokenType.NAME_SEPARATOR);

        JsonToken token = tokenizer.nextToken();
        assertEquals(JsonTokenType.BOOLEAN, token.type());
        assertEquals("true", token.value());

        this.assertTokenTypes(tokenizer, JsonTokenType.VALUE_SEPARATOR, JsonTokenType.STRING, JsonTokenType.NAME_SEPARATOR);

        token = tokenizer.nextToken();
        assertEquals(JsonTokenType.NULL, token.type());
        assertEquals("null", token.value());

        this.assertTokenTypes(tokenizer, JsonTokenType.VALUE_SEPARATOR, JsonTokenType.STRING, JsonTokenType.NAME_SEPARATOR);

        token = tokenizer.nextToken();
        assertEquals(JsonTokenType.NUMBER, token.type());
        assertEquals("10", token.value());

        this.assertTokenTypes(tokenizer, JsonTokenType.VALUE_SEPARATOR, JsonTokenType.STRING, JsonTokenType.NAME_SEPARATOR);

        try {
            tokenizer.nextToken();
            fail("Expected an exception");
        } catch (final IllegalStateException e) {
            assertEquals("Unexpected character: t", e.getMessage());
        }

        this.assertTokenTypes(tokenizer, JsonTokenType.VALUE_SEPARATOR, JsonTokenType.STRING, JsonTokenType.NAME_SEPARATOR);

        try {
            tokenizer.nextToken();
            fail("Expected an exception");
        } catch (final IllegalStateException e) {
            assertEquals("Unexpected character: n", e.getMessage());
        }
    }

}