package com.github.pokee.json.token;

import com.github.pokee.json.exception.TokenTypeExpectedException;
import com.github.pokee.json.value.JsonArray;
import com.github.pokee.json.value.JsonElement;
import com.github.pokee.json.value.JsonObject;
import com.github.pokee.json.value.JsonPrimitive;
import junit.framework.TestCase;

public class JsonParserTest extends TestCase {


    public void testParseReturnTypes() {
        record Case(String json, Class<?> expectedClass) {
        }
        final Case[] testCases = new Case[]{
                new Case("{\"key\":\"value\"}", JsonObject.class),
                new Case("[\"value1\", \"value2\"]", JsonArray.class),
                new Case("true", JsonPrimitive.class),
                new Case("false", JsonPrimitive.class),
                new Case("null", JsonPrimitive.class),
                new Case("123", JsonPrimitive.class),
                new Case("\"value\"", JsonPrimitive.class),
                new Case("!", null)
        };

        for (final Case testCase : testCases) {
            final JsonParser parser = new JsonParser(testCase.json());
            final JsonElement element;
            try {
                element = parser.parse();
                if (testCase.expectedClass == null) {
                    fail("Test Case " + testCase + "should have thrown an exception");
                    return;
                }
            } catch (TokenTypeExpectedException | IllegalStateException e) {
                if (testCase.expectedClass == null) {
                    continue;
                }
                fail("Test Case " + testCase + "got exception:" + e.getMessage());
                return;
            }
            assertEquals(testCase.expectedClass(), element.getClass());
        }
    }

    public void testParse() throws TokenTypeExpectedException {
        record Case(String json, Object... expectedFields) {
        }
        final Case[] testCases = new Case[]{
                new Case("{\"key\":\"value\"}", "key", "value"),
                new Case("{\"key\":123}", "key", 123),
                new Case("{\"key\":true}", "key", true),
                new Case("{\"key\":false}", "key", false),
                new Case("{\"key\":null}", "key", null),
                new Case("{\"key\":\"value\",\"key2\":\"value2\"}", "key", "value", "key2", "value2"),
        };

        for (final Case testCase : testCases) {
            final JsonParser parser = new JsonParser(testCase.json());
            final JsonObject object = (JsonObject) parser.parse();

            for (int i = 0; i < testCase.expectedFields().length; i += 2) {
                final String key = (String) testCase.expectedFields()[i];
                final Object value = testCase.expectedFields()[i + 1];
                assertTrue(object.has(key));

                final JsonPrimitive primitive = object.get(key).asPrimitive();
                if (primitive.isString()) {
                    assertEquals(value, primitive.asString());
                } else if (primitive.isInteger()) {
                    assertEquals(value, primitive.asInteger());
                } else if (primitive.isNull()) {
                    assertNull(value);
                } else if (primitive.isBoolean()) {
                    assertEquals(value, primitive.asBoolean());
                }
            }
        }
    }

    public void testObjectUnexpected() {
        final JsonParser parser = new JsonParser("{\"key\":}");
        try {
            parser.parse();
            fail("Expected an exception");
        } catch (final TokenTypeExpectedException e) {
            assertEquals(new TokenTypeExpectedException(new JsonTokenType[]{
                    JsonTokenType.BEGIN_OBJECT,
                    JsonTokenType.BEGIN_ARRAY,
                    JsonTokenType.STRING,
                    JsonTokenType.NUMBER,
                    JsonTokenType.BOOLEAN,
                    JsonTokenType.NULL
            }, JsonTokenType.END_OBJECT).getMessage(), e.getMessage());
        }
    }

    public void testUnexpectedStart() {
        final JsonParser parser = new JsonParser("}");
        try {
            parser.parse();
            fail("Expected an exception");
        } catch (final TokenTypeExpectedException e) {
            assertEquals(JsonTokenType.BEGIN_OBJECT, e.getExpected()[0]);
            assertEquals(JsonTokenType.END_OBJECT, e.getActual());
        }
    }

    public void testNestedObject() throws TokenTypeExpectedException {
        final JsonParser parser = new JsonParser("""
                {
                    "key1": "value1",
                    "key2": "value2",
                    "key3": {
                      "nestedKey1": "nestedValue1",
                      "nestedKey2": "nestedValue2"
                    }
                }
                """);
        final JsonObject object = (JsonObject) parser.parse();
        assertTrue(object.has("key3"));

        final JsonObject nestedObject = object.get("key3").asObject();
        assertEquals("nestedValue1", nestedObject.get("nestedKey1").asPrimitive().asString());
        assertEquals("nestedValue2", nestedObject.get("nestedKey2").asPrimitive().asString());
    }

    public void testArray() throws TokenTypeExpectedException {
        final JsonParser parser = new JsonParser("""
                ["value1", "value2"]
                """);
        final JsonArray array = (JsonArray) parser.parse();
        assertEquals("value1", array.get(0).asPrimitive().asString());
        assertEquals("value2", array.get(1).asPrimitive().asString());
    }

    public void testNestedArray() throws TokenTypeExpectedException {
        final JsonParser parser = new JsonParser("""
                {
                    "key1": "value1",
                    "key2": "value2",
                    "key3": ["nestedValue1", "nestedValue2", [false, [true, true, [1.23]]], {}]
                }
                """);
        final JsonObject object = (JsonObject) parser.parse();
        assertTrue(object.has("key3"));

        final JsonArray nestedArray = object.get("key3").asArray();
        assertEquals("nestedValue1", nestedArray.get(0).asPrimitive().asString());
        assertEquals("nestedValue2", nestedArray.get(1).asPrimitive().asString());

        assertTrue(nestedArray.get(2).isArray());
        final JsonArray level1 = nestedArray.get(2).asArray();
        assertEquals(2, level1.size());
        assertFalse(level1.get(0).asPrimitive().asBoolean());

        assertTrue(level1.get(1).isArray());
        final JsonArray level2 = level1.get(1).asArray();
        assertEquals(3, level2.size());
        assertTrue(level2.get(0).asPrimitive().asBoolean());
        assertTrue(level2.get(1).asPrimitive().asBoolean());

        assertTrue(level2.get(2).isArray());
        final JsonArray level3 = level2.get(2).asArray();
        assertEquals(1, level3.size());
        assertEquals(1.23, level3.get(0).asPrimitive().asDouble());

        assertTrue(nestedArray.get(3).isObject());
    }

    public void testArrayUnexpected() {
        final JsonParser parser = new JsonParser("[}]");
        try {
            parser.parse();
            fail("Expected an exception");
        } catch (final TokenTypeExpectedException | IllegalStateException e) {
            assertEquals(new TokenTypeExpectedException(new JsonTokenType[]{
                    JsonTokenType.BEGIN_OBJECT,
                    JsonTokenType.BEGIN_ARRAY,
                    JsonTokenType.STRING,
                    JsonTokenType.NUMBER,
                    JsonTokenType.BOOLEAN,
                    JsonTokenType.NULL
            }, JsonTokenType.END_OBJECT).getMessage(), e.getMessage());
        }
    }

    public void testUnexpectedEnd() {
        final JsonParser parser = new JsonParser("{},");
        try {
            parser.parse();
            fail("Expected an exception");
        } catch (final TokenTypeExpectedException e) {
            assertEquals(JsonTokenType.END_DOCUMENT, e.getExpected()[0]);
            assertEquals(JsonTokenType.VALUE_SEPARATOR, e.getActual());
        }
    }

    public void testExpect() {
        final JsonParser parser = new JsonParser("{");
        try {
            parser.parse();
            fail("Expected an exception");
        } catch (final TokenTypeExpectedException e) {
            assertEquals(JsonTokenType.STRING, e.getExpected()[0]);
            assertEquals(JsonTokenType.END_DOCUMENT, e.getActual());
        }
    }

}