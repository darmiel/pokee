package com.github.pokee.json.parser;

import com.github.pokee.json.exception.TokenTypeExpectedException;
import com.github.pokee.json.value.JsonElement;
import com.github.pokee.json.value.JsonFunction;

public interface FunctionCallback {

    JsonElement run(final JsonParser parser, final JsonFunction function) throws TokenTypeExpectedException;

}
