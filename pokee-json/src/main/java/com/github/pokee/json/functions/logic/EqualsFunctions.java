package com.github.pokee.json.functions.logic;

import com.github.pokee.json.exception.TokenTypeExpectedException;
import com.github.pokee.json.functions.FunctionCallback;
import com.github.pokee.json.parser.JsonParser;
import com.github.pokee.json.value.JsonElement;
import com.github.pokee.json.value.JsonFunction;
import com.github.pokee.json.value.JsonPrimitive;

public class EqualsFunctions {

    public static class EqualsFunction implements FunctionCallback {

        public static final String NAME = "eq";
        public static final EqualsFunction INSTANCE = new EqualsFunction();

        @Override
        public JsonElement run(final JsonParser parser, final JsonFunction function) throws TokenTypeExpectedException {
            assertMinParameterCount(function, 2);

            final JsonElement a = function.getParameter(0);
            final JsonElement b = function.getParameter(1);

            if (a.isObject() != b.isObject()) {
                return JsonPrimitive.fromBool(false);
            }
            if (a.isObject()) {
                final int sizeA = a.asObject().entries().size();
                // we cannot compare objects yet, but if they have different sizes, they are different
                if (sizeA != b.asObject().entries().size()) {
                    return JsonPrimitive.fromBool(false);
                }
                if (sizeA == 0) {
                    return JsonPrimitive.fromBool(true);
                }
                throw new UnsupportedOperationException("Cannot compare objects");
            }

            if (a.isArray() != b.isArray()) {
                return JsonPrimitive.fromBool(false);
            }
            if (a.isArray()) {
                // we cannot compare arrays yet, but if they have different sizes, they are different
                if (a.asArray().size() != b.asArray().size()) {
                    return JsonPrimitive.fromBool(false);
                }
                if (a.asArray().size() == 0) {
                    return JsonPrimitive.fromBool(true);
                }
                throw new UnsupportedOperationException("Cannot compare arrays");
            }

            if (a.isPrimitive() != b.isPrimitive()) {
                return JsonPrimitive.fromBool(false);
            }
            if (a.isPrimitive()) {
                final JsonPrimitive aPrimitive = a.asPrimitive();
                final JsonPrimitive bPrimitive = b.asPrimitive();

                if (aPrimitive.isNull() != bPrimitive.isNull()) {
                    return JsonPrimitive.fromBool(false);
                }
                if (aPrimitive.isBoolean() != bPrimitive.isBoolean()) {
                    return JsonPrimitive.fromBool(false);
                }
                if (aPrimitive.isString() != bPrimitive.isString()) {
                    return JsonPrimitive.fromBool(false);
                }
                if (aPrimitive.isDouble() != bPrimitive.isDouble()) {
                    return JsonPrimitive.fromBool(false);
                }
                if (aPrimitive.isInteger() != bPrimitive.isInteger()) {
                    return JsonPrimitive.fromBool(false);
                }

                if (aPrimitive.isNull()) {
                    return JsonPrimitive.fromBool(true);
                }
                if (aPrimitive.isBoolean()) {
                    return JsonPrimitive.fromBool(aPrimitive.asBoolean() == bPrimitive.asBoolean());
                }
                if (aPrimitive.isString()) {
                    return JsonPrimitive.fromBool(aPrimitive.asString().equals(bPrimitive.asString()));
                }
                if (aPrimitive.isDouble()) {
                    return JsonPrimitive.fromBool(aPrimitive.asDouble() == bPrimitive.asDouble());
                }
                if (aPrimitive.isInteger()) {
                    return JsonPrimitive.fromBool(aPrimitive.asInteger() == bPrimitive.asInteger());
                }

                throw new IllegalStateException("The primitive is not a null, boolean, string, double or integer");
            }

            throw new IllegalStateException("The element is not an object, array or primitive");
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
