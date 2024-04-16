package com.github.pokee.pson.parser;

import com.github.pokee.pson.exception.TokenTypeExpectedException;
import com.github.pokee.pson.value.*;

import java.util.ArrayList;
import java.util.List;


public class JsonParser {

    private final JsonTokenizer tokenizer;
    private final boolean expandFunctions;
    private final JsonFunctionRunner functionRunner;

    /**
     * Creates a new parser with the given JSON string
     *
     * @param json            the JSON string
     * @param expandFunctions whether to expand functions
     * @param functionRunner  the function runner
     */
    public JsonParser(final String json, final boolean expandFunctions, final JsonFunctionRunner functionRunner) {
        this.tokenizer = new JsonTokenizer(json);
        this.expandFunctions = expandFunctions;
        this.functionRunner = functionRunner;
    }

    /**
     * Creates a new parser with the given JSON string
     *
     * @param json           the JSON string
     * @param functionRunner the function runner
     */
    public JsonParser(final String json, final JsonFunctionRunner functionRunner) {
        this(json, false, functionRunner);
    }

    /**
     * Creates a new parser with the given JSON string using the default function runner.
     *
     * @param json            the JSON string
     * @param expandFunctions whether to expand functions
     */
    public JsonParser(final String json, final boolean expandFunctions) {
        this(json, expandFunctions, JsonFunctionRunner.defaultFunctionRunner());
    }

    /**
     * Creates a new parser with the given JSON string using the default function runner
     *
     * @param json the JSON string
     */
    public JsonParser(final String json) {
        this(json, JsonFunctionRunner.defaultFunctionRunner());
    }

    /**
     * Returns a copy of the parser with the given JSON string
     *
     * @param json the JSON string
     * @return a copy of the parser with the given JSON string
     */
    public JsonParser copyConfiguration(final String json) {
        return new JsonParser(json, this.expandFunctions, this.functionRunner);
    }

    /**
     * Parses the JSON string and returns the parsed JSON as a {@link JsonElement}
     * The JSON string is expected to be a valid JSON document
     * and the tokenizer is expected to be at the beginning of the document.
     * <br>
     * The tokenizer will be at the end of the document after this method returns
     * the parsed JSON
     *
     * @param expectDocumentEnd whether to expect the end of the document
     * @return the parsed JSON as a {@link JsonElement}
     * @throws TokenTypeExpectedException if the tokenizer does not contain a valid JSON document
     */
    public JsonElement parse(final boolean expectDocumentEnd) throws TokenTypeExpectedException {
        final JsonElement element;
        final JsonToken peekedToken = this.tokenizer.peekNextToken();

        switch (peekedToken.type()) {
            case BEGIN_OBJECT -> element = this.readObject();
            case BEGIN_ARRAY -> element = this.readArray();
            case STRING, NUMBER, BOOLEAN, NULL -> {
                peekedToken.apply(this.tokenizer);
                element = new JsonPrimitive(peekedToken.value().strip());
            }
            case BEGIN_FUNCTION -> {
                final JsonFunction function = this.readFunction();
                if (this.expandFunctions) {
                    if (this.functionRunner == null) {
                        throw new NullPointerException("Cannot expand functions because function runner is null");
                    }
                    element = this.functionRunner.runFunction(this, function);
                } else {
                    element = function;
                }
            }
            default -> throw new TokenTypeExpectedException(new JsonTokenType[]{
                    JsonTokenType.BEGIN_OBJECT,
                    JsonTokenType.BEGIN_ARRAY,
                    JsonTokenType.STRING,
                    JsonTokenType.NUMBER,
                    JsonTokenType.BOOLEAN,
                    JsonTokenType.NULL
            }, peekedToken.type());
        }

        // expect the end of the document
        if (expectDocumentEnd) {
            final JsonToken endDocumentToken = this.tokenizer.nextToken();
            if (endDocumentToken.type() != JsonTokenType.END_DOCUMENT) {
                throw new TokenTypeExpectedException(JsonTokenType.END_DOCUMENT, endDocumentToken.type());
            }
        }

        return element;
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
        return this.parse(true);
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
     *
     * @return the array read from the tokenizer
     * @throws TokenTypeExpectedException if the tokenizer does not contain a valid array
     */
    private JsonArray readArray() throws TokenTypeExpectedException {
        this.expect(JsonTokenType.BEGIN_ARRAY);

        final JsonArray array = new JsonArray();

        final JsonToken peekNextToken = this.tokenizer.peekNextToken();
        if (peekNextToken.type() != JsonTokenType.END_ARRAY) {
            while (true) {
                array.add(this.parse(false));

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
                final String key = keyToken.value().substring(1, keyToken.value().length() - 1);
                this.expect(JsonTokenType.NAME_SEPARATOR);

                object.put(key, this.parse(false));

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

    /**
     * Read a function from the tokenizer
     * <p>
     * The tokenizer is expected to be at the beginning of a function
     * and will be at the end of the function after this method returns
     * the function.
     * <br>
     * The expected format is:
     * <pre>
     *         \@functionName(param1, param2, param3)
     *         </pre>
     *
     * @return the function read from the tokenizer
     * @throws TokenTypeExpectedException if the tokenizer does not contain a valid function
     */
    private JsonFunction readFunction() throws TokenTypeExpectedException {
        this.expect(JsonTokenType.BEGIN_FUNCTION);
        final String functionName = JsonFunctionRunner.transformFunctionName(this.tokenizer.readFunctionName());
        this.expect(JsonTokenType.LPAREN);

        final List<JsonElement> parameters = new ArrayList<>();

        while (true) {
            final JsonToken peekNextToken = this.tokenizer.peekNextToken();

            // if the next token is a right parenthesis, the function has no parameters
            if (peekNextToken.type() == JsonTokenType.RPAREN) {
                break;
            }

            final JsonElement parameter = this.parse(false);
            parameters.add(parameter);

            final JsonToken peekNextValue = this.tokenizer.peekNextToken();
            if (peekNextValue.type() == JsonTokenType.VALUE_SEPARATOR) {
                this.expect(JsonTokenType.VALUE_SEPARATOR);
            } else {
                break;
            }
        }

        this.expect(JsonTokenType.RPAREN);
        return new JsonFunction(functionName, parameters);
    }

}
