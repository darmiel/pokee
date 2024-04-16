package com.github.pokee.json.functions.logic;

import com.github.pokee.json.exception.TokenTypeExpectedException;
import com.github.pokee.json.functions.FunctionCallback;
import com.github.pokee.json.parser.JsonParser;
import com.github.pokee.json.value.JsonElement;
import com.github.pokee.json.value.JsonFunction;

/**
 * Function to return the first parameter if it is null or false, otherwise the second parameter.
 */
public class DefaultFunction implements FunctionCallback {

    public static final String NAME = "default";
    public static final DefaultFunction INSTANCE = new DefaultFunction();

    @Override
    public JsonElement run(JsonParser parser, JsonFunction function) throws TokenTypeExpectedException {
        this.assertParameterCount(function, 2);

        final JsonElement nullishElement = function.getParameter(0);

        final boolean isNullOrFalse;
        if (nullishElement.isPrimitive() && nullishElement.asPrimitive().isNull()) {
            isNullOrFalse = true;
        } else {
            isNullOrFalse = !IfFunction.getTruthy(nullishElement);
        }

        return isNullOrFalse ? function.getParameter(1) : nullishElement;
    }

}
