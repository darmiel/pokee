package com.github.pokee.pson.functions.logic;

import com.github.pokee.pson.exception.TokenTypeExpectedException;
import com.github.pokee.pson.functions.FunctionCallback;
import com.github.pokee.pson.parser.JsonParser;
import com.github.pokee.pson.value.JsonElement;
import com.github.pokee.pson.value.JsonFunction;

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
