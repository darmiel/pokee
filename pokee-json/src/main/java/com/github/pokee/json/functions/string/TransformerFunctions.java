package com.github.pokee.json.functions.string;

import com.github.pokee.json.exception.TokenTypeExpectedException;
import com.github.pokee.json.functions.FunctionCallback;
import com.github.pokee.json.parser.JsonParser;
import com.github.pokee.json.value.*;

import java.util.Map;
import java.util.function.Function;

public class TransformerFunctions {

    /**
     * Run the transformer function.
     * If the element is an array, apply the transformer function to every element
     * If the element is an object, apply the transformer function to every key/value
     * If the element is a primitive, apply the transformer function to the value
     *
     * @param parser           the parser
     * @param function         the function
     * @param transformer      the transformer
     * @param functionCallback the function callback
     * @return the transformed value
     * @throws TokenTypeExpectedException if the token type is not expected
     */
    public static JsonElement run(
            final JsonParser parser,
            final JsonFunction function,
            final Function<String, String> transformer,
            final FunctionCallback functionCallback
    ) throws TokenTypeExpectedException {
        final JsonElement parameter = function.getParameter(0);
        final JsonObject options = function.getOptions();

        // if the parameter is an array, apply the upper case function to every element
        if (parameter.isArray()) {
            final JsonArray originalArray = parameter.asArray();
            final JsonArray newArray = new JsonArray();
            for (int i = 0; i < originalArray.size(); i++) {
                newArray.add(functionCallback.run(parser, function.copyExceptReplace(0, originalArray.get(i))));
            }
            return newArray;
        }

        // if the parameter is an object, apply the upper case function to every key/value
        if (parameter.isObject()) {
            boolean transformKeys = options.has("keys") && options.get("keys").asPrimitive().asBoolean();
            boolean transformValues = options.has("values") && options.get("values").asPrimitive().asBoolean();
            if (!transformKeys && !transformValues) {
                transformValues = true;
            }
            final JsonObject originalObject = parameter.asObject();
            final JsonObject newObject = new JsonObject();

            for (final Map.Entry<String, JsonElement> entry : originalObject.entries()) {
                final String key = transformKeys ? transformer.apply(entry.getKey()) : entry.getKey();
                final JsonElement value = transformValues
                        ? functionCallback.run(parser, function.copyExceptReplace(0, entry.getValue()))
                        : entry.getValue();
                newObject.put(key, value);
            }

            return newObject;
        }

        final JsonPrimitive primitive = parameter.asPrimitive();
        if (primitive.isString()) {
            return JsonPrimitive.fromString(transformer.apply(primitive.asString()));
        }
        return primitive;
    }

    /**
     * This function returns the upper case of a string
     * <p>
     * The first parameter is the string to transform
     * The second parameter is an object with the following optional fields:
     * - keys: boolean (default: false)
     * - values: boolean (default: true)
     * If the parameter is an array, it applies the upper case function to every element
     * If the parameter is an object, it applies the upper case function to every key/value
     * If the parameter is a primitive, it applies the upper case function to the value
     * </p>
     */
    public static class ToUpper implements FunctionCallback {
        public static final String NAME = "to-upper";
        public static final ToUpper INSTANCE = new ToUpper();

        @Override
        public JsonElement run(final JsonParser parser, final JsonFunction function) throws TokenTypeExpectedException {
            assertMinParameterCount(function, 1);
            return TransformerFunctions.run(parser, function, String::toUpperCase, this);
        }
    }

    /**
     * This function returns the lower case of a string
     * <p>
     * The first parameter is the string to transform
     * The second parameter is an object with the following optional fields:
     * - keys: boolean (default: false)
     * - values: boolean (default: true)
     * If the parameter is an array, it applies the lower case function to every element
     * If the parameter is an object, it applies the lower case function to every key/value
     * If the parameter is a primitive, it applies the lower case function to the value
     * </p>
     */
    public static class ToLower implements FunctionCallback {
        public static final String NAME = "to-lower";
        public static final ToLower INSTANCE = new ToLower();

        @Override
        public JsonElement run(final JsonParser parser, final JsonFunction function) throws TokenTypeExpectedException {
            assertMinParameterCount(function, 1);
            return TransformerFunctions.run(parser, function, String::toLowerCase, this);
        }
    }

}
