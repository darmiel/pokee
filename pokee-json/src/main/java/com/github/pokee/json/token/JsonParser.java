package com.github.pokee.json.token;

import com.github.pokee.json.exception.TokenTypeExpectedException;
import com.github.pokee.json.value.JsonObject;
import com.github.pokee.json.value.JsonArray;
import com.github.pokee.json.value.JsonElement;
import com.github.pokee.json.value.JsonPrimitive;

public class JsonParser {

    private final JsonTokenizer tokenizer;

    public JsonParser(final String json) {
        this.tokenizer = new JsonTokenizer(json);
    }

    /**
     * Parses the JSON string and returns the parsed JSON as a {@link JsonElement}
     * The JSON string is expected to be a valid JSON document
     * and the tokenizer is expected to be at the beginning of the document.
     * <br>
     * The tokenizer will be at the end of the document after this method returns
     * the parsed JSON
     *
     * @return the parsed JSON as a {@link JsonElement}
     * @throws TokenTypeExpectedException if the tokenizer does not contain a valid JSON document
     */
    public JsonElement parse() throws TokenTypeExpectedException {
        final JsonElement element;
        final JsonToken peakedToken = this.tokenizer.peekNextToken();

        switch (peakedToken.type()) {
            case BEGIN_OBJECT -> {
                element = this.readObject();
            }
            case BEGIN_ARRAY -> {
                element = this.readArray();
            }
            case STRING, NUMBER, BOOLEAN, NULL -> {
                final JsonToken token = this.tokenizer.nextToken();
                element = new JsonPrimitive(token.value());
            }
            default -> throw new TokenTypeExpectedException(new JsonTokenType[]{
                    JsonTokenType.BEGIN_OBJECT,
                    JsonTokenType.BEGIN_ARRAY,
                    JsonTokenType.STRING,
                    JsonTokenType.NUMBER,
                    JsonTokenType.BOOLEAN,
                    JsonTokenType.NULL
            }, peakedToken.type());
        }

        // expect the end of the document
        final JsonToken endDocumentToken = this.tokenizer.nextToken();
        if (endDocumentToken.type() != JsonTokenType.END_DOCUMENT) {
            throw new TokenTypeExpectedException(JsonTokenType.END_DOCUMENT, endDocumentToken.type());
        }

        return element;
    }

    /**
     * Expects the next token to be of the given type.
     * If the next token is not of the given type, a {@link TokenTypeExpectedException} is thrown
     * containing the expected type and the actual type
     * <br>
     * If the next token is of the given type, it is returned
     * and the tokenizer is advanced to the next token
     * <br>
     * This method does not handle the case where the tokenizer is at the end of the document
     * and the next token is expected to be of the given type
     *
     * @param type the expected type of the next token
     * @return the next (expected) token
     * @throws TokenTypeExpectedException if the next token is not of the given type
     */
    private JsonToken expect(final JsonTokenType type) throws TokenTypeExpectedException {
        final JsonToken token = this.tokenizer.nextToken();
        if (token.type() != type) {
            throw new TokenTypeExpectedException(type, token.type());
        }
        return token;
    }

    /**
     * Reads an array from the tokenizer
     * <p>
     * The tokenizer is expected to be at the beginning of an array
     * and will be at the end of the array after this method returns
     * the array.
     * <br>
     * The expected format is:
     * <pre>
     *         [
     *           "value1",
     *           "value2",
     *           {
     *             "nestedKey1": "nestedValue1",
     *             "nestedKey2": "nestedValue2"
     *           }
     *         ]
     * </pre>
     * @return the array read from the tokenizer
     * @throws TokenTypeExpectedException if the tokenizer does not contain a valid array
     */
    private JsonArray readArray() throws TokenTypeExpectedException {
        this.expect(JsonTokenType.BEGIN_ARRAY);

        final JsonArray array = new JsonArray();

        final JsonToken peekNextToken = this.tokenizer.peekNextToken();
        if (peekNextToken.type() != JsonTokenType.END_ARRAY) {
            while (true) {
                final JsonToken peekValueToken = this.tokenizer.peekNextToken();

                switch (peekValueToken.type()) {
                    case BEGIN_OBJECT -> {
                        final JsonObject nestedObject = this.readObject();
                        array.add(nestedObject);
                    }
                    case BEGIN_ARRAY -> {
                        final JsonArray nestedArray = this.readArray();
                        array.add(nestedArray);
                    }
                    case STRING, NUMBER, BOOLEAN, NULL -> {
                        final JsonToken valueToken = this.tokenizer.nextToken();
                        array.add(new JsonPrimitive(valueToken.value()));
                    }
                    default -> throw new TokenTypeExpectedException(new JsonTokenType[]{
                            JsonTokenType.BEGIN_OBJECT,
                            JsonTokenType.BEGIN_ARRAY,
                            JsonTokenType.STRING,
                            JsonTokenType.NUMBER,
                            JsonTokenType.BOOLEAN,
                            JsonTokenType.NULL
                    }, peekValueToken.type());
                }

                final JsonToken peekNextValue = this.tokenizer.peekNextToken();
                if (peekNextValue.type() == JsonTokenType.VALUE_SEPARATOR) {
                    this.expect(JsonTokenType.VALUE_SEPARATOR);
                } else {
                    break;
                }
            }
        }

        this.expect(JsonTokenType.END_ARRAY);
        return array;
    }

    /**
     * Reads an object from the tokenizer
     * <p>
     * The tokenizer is expected to be at the beginning of an object
     * and will be at the end of the object after this method returns
     * the object.
     * <br>
     * The expected format is:
     * <pre>
     *         {
     *           "key1": "value1",
     *           "key2": "value2",
     *           "key3": {
     *             "nestedKey1": "nestedValue1",
     *             "nestedKey2": "nestedValue2"
     *           }
     *         }
     *         </pre>
     *
     * @return the object read from the tokenizer
     * @throws TokenTypeExpectedException if the tokenizer does not contain a valid object
     */
    private JsonObject readObject() throws TokenTypeExpectedException {
        this.expect(JsonTokenType.BEGIN_OBJECT);

        final JsonObject object = new JsonObject();

        final JsonToken peekNextToken = this.tokenizer.peekNextToken();
        if (peekNextToken.type() != JsonTokenType.END_OBJECT) {
            while (true) {
                final JsonToken keyToken = this.expect(JsonTokenType.STRING);

                // remove the quotes from the key
                final String key = keyToken.value().substring(1, keyToken.value().length() - 1);

                this.expect(JsonTokenType.NAME_SEPARATOR);

                final JsonToken peekValueToken = this.tokenizer.peekNextToken();
                switch (peekValueToken.type()) {
                    case BEGIN_OBJECT -> {
                        final JsonObject nestedObject = this.readObject();
                        object.put(key, nestedObject);
                    }
                    case BEGIN_ARRAY -> {
                        final JsonArray nestedArray = this.readArray();
                        object.put(key, nestedArray);
                    }
                    case STRING, NUMBER, BOOLEAN, NULL -> {
                        final JsonToken valueToken = this.tokenizer.nextToken();
                        object.put(key, new JsonPrimitive(valueToken.value()));
                    }
                    default -> throw new TokenTypeExpectedException(new JsonTokenType[]{
                            JsonTokenType.BEGIN_OBJECT,
                            JsonTokenType.BEGIN_ARRAY,
                            JsonTokenType.STRING,
                            JsonTokenType.NUMBER,
                            JsonTokenType.BOOLEAN,
                            JsonTokenType.NULL
                    }, peekValueToken.type());
                }

                final JsonToken peekNextValue = this.tokenizer.peekNextToken();
                if (peekNextValue.type() == JsonTokenType.VALUE_SEPARATOR) {
                    this.expect(JsonTokenType.VALUE_SEPARATOR);
                } else {
                    break;
                }
            }
        }

        this.expect(JsonTokenType.END_OBJECT);

        return object;
    }

}
