package com.github.pokee.json.functions.load;

import com.github.pokee.json.exception.TokenTypeExpectedException;
import com.github.pokee.json.functions.FunctionCallback;
import com.github.pokee.json.parser.JsonParser;
import com.github.pokee.json.value.JsonElement;
import com.github.pokee.json.value.JsonFunction;

/**
 * This function returns the parsed value of a json-encoded environment variable
 * <p>
 * The first parameter is the name of the environment variable
 * The second parameter is an object with the following optional fields:
 * - optional: boolean (default: false)
 * - default: any (default: null)
 * If the environment variable is not found, and optional is true, it returns null
 * If the environment variable is not found, and optional is false, it throws an exception
 * If the environment variable is found it parses the value as JSON
 * </p>
 */
public class EnvironmentAsJsonFunction implements FunctionCallback {

    public static final String NAME = "json-env";
    public static final EnvironmentAsJsonFunction INSTANCE = new EnvironmentAsJsonFunction();


    // this is nearly identical to the environment function
    @Override
    public JsonElement run(final JsonParser parser, final JsonFunction function) throws TokenTypeExpectedException {
        assertMinParameterCount(function, 1);
        return EnvironmentFunction.run(function, (options, value) -> parser.copyConfiguration(value).parse());
    }

}
