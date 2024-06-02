package com.github.pokee.pson.functions.logic;

import com.github.pokee.pson.exception.TokenTypeExpectedException;
import com.github.pokee.pson.functions.FunctionCallback;
import com.github.pokee.pson.parser.JsonParser;
import com.github.pokee.pson.value.JsonElement;
import com.github.pokee.pson.value.JsonFunction;
import com.github.pokee.pson.value.JsonPrimitive;

public class EqualsFunctions {

    /**
     * Function to compare two elements for equality.
     */
    public static class EqualsFunction implements FunctionCallback {

        public static final String NAME = "eq";
        public static final EqualsFunction INSTANCE = new EqualsFunction();

        @Override
        public JsonElement run(final JsonParser parser, final JsonFunction function) throws TokenTypeExpectedException {
            assertMinParameterCount(function, 2);

            final JsonElement a = function.getParameter(0);
            final JsonElement b = function.getParameter(1);

            if (a.isObject() && b.isObject()) {
                return compareObjects(a, b);
            } else if (a.isArray() && b.isArray()) {
                return compareArrays(a, b);
            } else if (a.isPrimitive() && b.isPrimitive()) {
                return comparePrimitives(a.asPrimitive(), b.asPrimitive());
            }

            return JsonPrimitive.fromBool(false);
        }

        private JsonElement compareObjects(JsonElement a, JsonElement b) {
            final int sizeA = a.asObject().entries().size();
            final int sizeB = b.asObject().entries().size();

            if (sizeA != sizeB) {
                return JsonPrimitive.fromBool(false);
            }
            if (sizeA == 0) {
                return JsonPrimitive.fromBool(true);
            }

            throw new UnsupportedOperationException("Cannot compare objects");
        }

        private JsonElement compareArrays(JsonElement a, JsonElement b) {
            final int sizeA = a.asArray().size();
            final int sizeB = b.asArray().size();

            if (sizeA != sizeB) {
                return JsonPrimitive.fromBool(false);
            }
            if (sizeA == 0) {
                return JsonPrimitive.fromBool(true);
            }

            throw new UnsupportedOperationException("Cannot compare arrays");
        }

        private JsonElement comparePrimitives(JsonPrimitive aPrimitive, JsonPrimitive bPrimitive) {
            if (aPrimitive.isNull() && bPrimitive.isNull()) {
                return JsonPrimitive.fromBool(true);
            }
            if (aPrimitive.isBoolean() && bPrimitive.isBoolean()) {
                return JsonPrimitive.fromBool(aPrimitive.asBoolean() == bPrimitive.asBoolean());
            }
            if (aPrimitive.isString() && bPrimitive.isString()) {
                return JsonPrimitive.fromBool(aPrimitive.asString().equals(bPrimitive.asString()));
            }
            if (aPrimitive.isDouble() && bPrimitive.isDouble()) {
                return JsonPrimitive.fromBool(aPrimitive.asDouble() == bPrimitive.asDouble());
            }
            if (aPrimitive.isInteger() && bPrimitive.isInteger()) {
                return JsonPrimitive.fromBool(aPrimitive.asInteger() == bPrimitive.asInteger());
            }

            return JsonPrimitive.fromBool(false);
        }

    }

    private interface NumberCallback {
        boolean compare(final Number a, final Number b);
    }

    private static JsonElement run(final JsonFunction function, final NumberCallback callback) {
        final JsonElement a = function.getParameter(0);
        final JsonElement b = function.getParameter(1);

        if (!a.isPrimitive() || !b.isPrimitive()) {
            throw new IllegalStateException("The elements are not both double or integer");
        }

        final JsonPrimitive aPrimitive = a.asPrimitive();
        final JsonPrimitive bPrimitive = b.asPrimitive();

        if (aPrimitive.isInteger() && bPrimitive.isInteger()) {
            return JsonPrimitive.fromBool(callback.compare(aPrimitive.asInteger(), bPrimitive.asInteger()));
        }
        if (aPrimitive.isInteger() && bPrimitive.isDouble()) {
            return JsonPrimitive.fromBool(callback.compare(aPrimitive.asInteger(), bPrimitive.asDouble()));
        }
        if (aPrimitive.isDouble() && bPrimitive.isInteger()) {
            return JsonPrimitive.fromBool(callback.compare(aPrimitive.asDouble(), bPrimitive.asInteger()));
        }

        throw new IllegalStateException("can only compare numbers");
    }


    public static class GreaterThan implements FunctionCallback {

        public static final String NAME = "gt";
        public static final GreaterThan INSTANCE = new GreaterThan();

        @Override
        public JsonElement run(final JsonParser parser, final JsonFunction function) throws TokenTypeExpectedException {
            assertMinParameterCount(function, 2);
            return EqualsFunctions.run(function, (a, b) -> a.doubleValue() > b.doubleValue());
        }

    }

    public static class GreaterThanOrEquals implements FunctionCallback {

        public static final String NAME = "gte";
        public static final GreaterThanOrEquals INSTANCE = new GreaterThanOrEquals();

        @Override
        public JsonElement run(final JsonParser parser, final JsonFunction function) throws TokenTypeExpectedException {
            assertMinParameterCount(function, 2);
            return EqualsFunctions.run(function, (a, b) -> a.doubleValue() >= b.doubleValue());
        }

    }

    public static class LessThan implements FunctionCallback {

        public static final String NAME = "lt";
        public static final LessThan INSTANCE = new LessThan();

        @Override
        public JsonElement run(final JsonParser parser, final JsonFunction function) throws TokenTypeExpectedException {
            assertMinParameterCount(function, 2);
            return EqualsFunctions.run(function, (a, b) -> a.doubleValue() < b.doubleValue());
        }

    }

    public static class LessThanOrEquals implements FunctionCallback {

        public static final String NAME = "lte";
        public static final LessThanOrEquals INSTANCE = new LessThanOrEquals();

        @Override
        public JsonElement run(final JsonParser parser, final JsonFunction function) throws TokenTypeExpectedException {
            assertMinParameterCount(function, 2);
            return EqualsFunctions.run(function, (a, b) -> a.doubleValue() <= b.doubleValue());
        }

    }

}
