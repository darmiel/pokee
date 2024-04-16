package com.github.pokee.pson.functions.logic;

import com.github.pokee.pson.exception.TokenTypeExpectedException;
import com.github.pokee.pson.functions.FunctionCallback;
import com.github.pokee.pson.parser.JsonParser;
import com.github.pokee.pson.value.JsonElement;
import com.github.pokee.pson.value.JsonFunction;
import com.github.pokee.pson.value.JsonPrimitive;

public class BooleanFunctions {

    /**
     * Function to negate a boolean
     */
    public static class IsFalseFunction implements FunctionCallback {
        public static final String NAME = "not";

        public static final IsFalseFunction INSTANCE = new IsFalseFunction();

        @Override
        public JsonElement run(final JsonParser parser, final JsonFunction function) throws TokenTypeExpectedException {
            assertMinParameterCount(function, 1);
            return JsonPrimitive.fromBool(!IfFunction.getTruthy(function.getParameter(0)));
        }
    }

    /**
     * Function to return the truthy value of a value
     */
    public static class IsTrueFunction implements FunctionCallback {
        public static final String NAME = "is-true";

        public static final IsTrueFunction INSTANCE = new IsTrueFunction();

        @Override
        public JsonElement run(final JsonParser parser, final JsonFunction function) throws TokenTypeExpectedException {
            assertMinParameterCount(function, 1);
            return JsonPrimitive.fromBool(IfFunction.getTruthy(function.getParameter(0)));
        }
    }

}
