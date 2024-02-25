package com.github.pokee.json.token;

public record JsonToken(JsonTokenType type, String value, int startPosition, int endPosition) {

    public void resetPosition(final JsonTokenizer tokenizer) {
        tokenizer.advanceTo(this.startPosition);
    }

    public void apply(final JsonTokenizer tokenizer) {
        tokenizer.advanceTo(this.endPosition);
    }

}
