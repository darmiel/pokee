package com.github.pokee.json.functions;

import com.github.pokee.json.exception.TokenTypeExpectedException;
import com.github.pokee.json.parser.JsonParser;
import com.github.pokee.json.value.JsonElement;
import com.github.pokee.json.value.JsonFunction;

public interface FunctionCallback {

    JsonElement run(final JsonParser parser, final JsonFunction function) throws TokenTypeExpectedException;

    default void assertMinParameterCount(final JsonFunction function, final int count) {
        if (function.getParameterCount() < count) {
            throw new IllegalArgumentException("Function " + function.getFunctionName() + " requires at least " + count + " parameters");
        }
    }

}
