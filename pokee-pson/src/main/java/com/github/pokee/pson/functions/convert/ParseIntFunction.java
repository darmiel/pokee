package com.github.pokee.pson.functions.convert;

import com.github.pokee.pson.exception.TokenTypeExpectedException;
import com.github.pokee.pson.functions.FunctionCallback;
import com.github.pokee.pson.parser.JsonParser;
import com.github.pokee.pson.value.JsonElement;
import com.github.pokee.pson.value.JsonFunction;
import com.github.pokee.pson.value.JsonPrimitive;

public class ParseIntFunction implements FunctionCallback {

    public static final String NAME = "int";
    public static final ParseIntFunction INSTANCE = new ParseIntFunction();

    @Override
    public JsonElement run(final JsonParser parser, final JsonFunction function) throws TokenTypeExpectedException {
        assertMinParameterCount(function, 1);

        final JsonElement element = function.getParameter(0);
        if (element.isPrimitive()) {
            final JsonPrimitive primitive = element.asPrimitive();
            if (primitive.isInteger()) {
                return primitive;
            }
            if (primitive.isBoolean()) {
                return JsonPrimitive.fromNumber(primitive.asBoolean() ? 1 : 0);
            }
            if (primitive.isString()) {
                final String value = primitive.asString();
                if (value.isBlank()) {
                    return JsonPrimitive.fromNull();
                }
                try {
                    return JsonPrimitive.fromNumber(Integer.parseInt(primitive.asString()));
                } catch (final NumberFormatException e) {
                    throw new IllegalStateException("Cannot parse int from string: " + primitive.asString(), e);
                }
            }
            if (primitive.isDouble()) {
                return JsonPrimitive.fromNumber((int) primitive.asDouble());
            }
        }
        if (element.isArray()) {
            return JsonPrimitive.fromNumber(element.asArray().size());
        }
        if (element.isObject()) {
            return JsonPrimitive.fromNumber(element.asObject().entries().size());
        }
        return null;
    }

}
