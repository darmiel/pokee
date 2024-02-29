package com.github.pokee.json.functions.load;

import com.github.pokee.json.exception.TokenTypeExpectedException;
import com.github.pokee.json.functions.FunctionCallback;
import com.github.pokee.json.functions.WithOptions;
import com.github.pokee.json.parser.JsonFunctionRunner;
import com.github.pokee.json.parser.JsonParser;
import com.github.pokee.json.value.JsonElement;
import com.github.pokee.json.value.JsonFunction;
import com.github.pokee.json.value.JsonObject;
import com.github.pokee.json.value.JsonPrimitive;

/**
 * This function returns the value of an environment variable
 * <p>
 * The first parameter is the name of the environment variable
 * The second parameter is an object with the following optional fields:
 * - optional: boolean (default: false)
 * - default: any (default: null)
 * - json: boolean (default: false)
 * If the environment variable is not found, and optional is true, it returns null
 * If the environment variable is not found, and optional is false, it throws an exception
 * If the environment variable is found, and json is true, it parses the value as JSON
 * If the environment variable is found, and json is false, it returns the value as a string
 * </p>
 */
public class EnvironmentFunction implements FunctionCallback {

    public static final String NAME = "env";
    public static final EnvironmentFunction INSTANCE = new EnvironmentFunction();

    public static final String JSON_OPTION = "json";

    public static JsonElement run(
            final JsonFunction function,
            final WithOptions<String, TokenTypeExpectedException> withOptions
    ) throws TokenTypeExpectedException {
        final String variableName = function.getParameter(0).asPrimitive().asString();
        final JsonObject options = function.getOptions();

        final String value = System.getenv(variableName);
        if (value == null) {
            return JsonFunctionRunner.getOrDefault(options, "Environment variable not found: " + variableName);
        }

        return withOptions.run(options, value);
    }

    // this is nearly identical to the JSON-environment function
    @Override
    public JsonElement run(final JsonParser parser, final JsonFunction function) throws TokenTypeExpectedException {
        assertMinParameterCount(function, 1);
        return EnvironmentFunction.run(function, (options, value) -> {
            if (options.has(JSON_OPTION) && options.get(JSON_OPTION).asPrimitive().asBoolean()) {
                return parser.copyConfiguration(value).parse();
            }

            return JsonPrimitive.fromString(value);
        });
    }

}
