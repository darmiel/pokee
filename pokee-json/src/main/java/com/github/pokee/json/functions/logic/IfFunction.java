package com.github.pokee.json.functions.logic;

import com.github.pokee.json.exception.TokenTypeExpectedException;
import com.github.pokee.json.functions.FunctionCallback;
import com.github.pokee.json.parser.JsonParser;
import com.github.pokee.json.value.JsonElement;
import com.github.pokee.json.value.JsonFunction;
import com.github.pokee.json.value.JsonPrimitive;

/**
 * If the first parameter is truthy, return the second parameter, otherwise return the third parameter
 */
public class IfFunction implements FunctionCallback {

    public static final String NAME = "if";
    public static final IfFunction INSTANCE = new IfFunction();

    /**
     * Returns true if the element is truthy
     *
     * @param element the element
     * @return true if the element is truthy
     */
    public static boolean getTruthy(final JsonElement element) {
        if (element.isPrimitive()) {
            final JsonPrimitive primitive = element.asPrimitive();
            if (primitive.isBoolean()) {
                return primitive.asBoolean();
            }
            if (primitive.isString()) {
                return !primitive.asString().isEmpty();
            }
            if (primitive.isDouble()) {
                return primitive.asDouble() != 0;
            }
            if (primitive.isInteger()) {
                return primitive.asInteger() != 0;
            }
            throw new IllegalStateException("The primitive is not a boolean, string, double or integer");
        }
        if (element.isObject()) {
            return !element.asObject().entries().isEmpty();
        }
        return element.asArray().size() > 0;
    }

    // @if(<truthy>, <"yes">, ["no"])
    @Override
    public JsonElement run(final JsonParser parser, final JsonFunction function) throws TokenTypeExpectedException {
        assertMinParameterCount(function, 2);

        final JsonElement truthyElement = function.getParameter(0);
        final boolean truthy = IfFunction.getTruthy(truthyElement);

        if (truthy) {
            return function.getParameter(1);
        }

        return function.getParameterCount() > 2
                ? function.getParameter(2)
                : JsonPrimitive.fromNull();
    }

}
