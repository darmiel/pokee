package com.github.pokee.json.parser;

/**
 * Represents a token in a JSON document
 * @param type The type of the token
 * @param value The value of the token
 * @param startPosition The start position of the token
 * @param endPosition The end position of the token
 */
public record JsonToken(JsonTokenType type, String value, int startPosition, int endPosition) {

    /**
     * Resets the tokenizer to the start position of this token
     * @param tokenizer the tokenizer to reset
     */
    public void resetPosition(final JsonTokenizer tokenizer) {
        tokenizer.advanceTo(this.startPosition);
    }

    /**
     * Advances the tokenizer to the end position of this token
     * @param tokenizer the tokenizer to advance
     */
    public void apply(final JsonTokenizer tokenizer) {
        tokenizer.advanceTo(this.endPosition);
    }

}
