package com.github.pokee.json.parser;

import com.github.pokee.json.exception.TokenTypeExpectedException;
import com.github.pokee.json.value.JsonElement;
import com.github.pokee.json.value.JsonFunction;
import com.github.pokee.json.value.JsonObject;
import com.github.pokee.json.value.JsonPrimitive;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to run a function
 */
public class JsonFunctionRunner {

    /**
     * If the value is present and parseJson is true, it parses the value as JSON
     * If the value is present and parseJson is false, it returns the value as a string
     * If the value is not present, and optional is true, it returns null
     * If the value is not present, and optional is false, it throws an exception
     *
     * @param value        the input value
     * @param options      the function options
     * @param parser       the parser
     * @param errorMessage the error message that will be thrown if the value is not present and optional is false
     * @return the parsed value
     * @throws TokenTypeExpectedException if the value is not present and optional is false
     */
    private static JsonElement defaultOptionalParseJson(
            final String value,
            final JsonObject options,
            final JsonParser parser,
            final String errorMessage
    ) throws TokenTypeExpectedException {
        if (value == null) {
            if (options.has("default")) {
                return options.get("default");
            }
            if (options.has("optional") && options.get("optional").asPrimitive().asBoolean()) {
                return JsonPrimitive.fromNull();
            }
            throw new IllegalArgumentException(errorMessage);
        }
        if (options.has("parseJson") && options.get("parseJson").asPrimitive().asBoolean()) {
            return parser.copyConfiguration(value).parse();
        }
        return JsonPrimitive.fromString(value);
    }

    /**
     * This function returns the value of an environment variable
     * <p>
     * The first parameter is the name of the environment variable
     * The second parameter is an object with the following optional fields:
     * - optional: boolean (default: false)
     * - default: any (default: null)
     * - parseJson: boolean (default: false)
     * If the environment variable is not found, and optional is true, it returns null
     * If the environment variable is not found, and optional is false, it throws an exception
     * If the environment variable is found, and parseJson is true, it parses the value as JSON
     * If the environment variable is found, and parseJson is false, it returns the value as a string
     * </p>
     */
    public static final FunctionCallback ENV_FUNCTION = (parser, function) -> {
        if (function.getParameterCount() == 0) {
            throw new RuntimeException("env function requires at least one parameter (variable_name: string)");
        }
        final String variableName = function.getParameter(0).asPrimitive().asString();
        final JsonObject options = function.getParameterCount() >
                1 ? function.getParameter(1).asObject()
                : new JsonObject();

        return JsonFunctionRunner.defaultOptionalParseJson(
                System.getenv(variableName),
                options,
                parser,
                "Environment variable not found: " + variableName
        );
    };

    /**
     * This function returns the contents of a file
     * <p>
     * The first parameter is the path to the file
     * The second parameter is an object with the following optional fields:
     * - optional: boolean (default: false)
     * - default: any (default: null)
     * - parseJson: boolean (default: false)
     * If the file is not found, and optional is true, it returns null
     * If the file is not found, and optional is false, it throws an exception
     * If the file is found, and parseJson is true, it parses the contents as JSON
     * If the file is found, and parseJson is false, it returns the contents as a string
     * </p>
     */
    public static final FunctionCallback FILE_FUNCTION = (parser, function) -> {
        if (function.getParameterCount() == 0) {
            throw new RuntimeException("file function requires at least one parameter (path: string)");
        }
        final String path = function.getParameter(0).asPrimitive().asString();
        final JsonObject options = function.getParameterCount() > 1
                ? function.getParameter(1).asObject()
                : new JsonObject();

        // read contents from file
        final String contents;
        try {
            contents = String.join("\n", Files.readAllLines(Paths.get(path)));
        } catch (IOException e) {
            throw new RuntimeException("File not found: " + path);
        }

        return JsonFunctionRunner.defaultOptionalParseJson(
                contents,
                options,
                parser,
                "File not found: " + path
        );
    };

    /**
     * The default JsonFunctionRunner contains the env and file functions
     */
    public static final JsonFunctionRunner DEFAULT = new JsonFunctionRunner(new HashMap<>())
            .registerFunctionCallback("env", ENV_FUNCTION)
            .registerFunctionCallback("file", FILE_FUNCTION);

    private final Map<String, FunctionCallback> functionCallbackMap;

    public JsonFunctionRunner(
            final Map<String, FunctionCallback> functionCallbackMap
    ) {
        this.functionCallbackMap = functionCallbackMap;
    }

    /**
     * Transform the function name to lowercase
     *
     * @param functionName the name of the function
     * @return the transformed function name
     */
    public static String transformFunctionName(final String functionName) {
        return functionName.strip().toLowerCase();
    }

    /**
     * Get the function callback for the given function name
     *
     * @param functionName the name of the function
     * @return the function callback
     */
    public FunctionCallback getFunctionCallback(final String functionName) {
        return functionCallbackMap.get(JsonFunctionRunner.transformFunctionName(functionName));
    }

    /**
     * Check if the function callback map contains the given function name
     *
     * @param functionName the name of the function
     * @return true if the function callback map contains the function name, false otherwise
     */
    public boolean hasFunctionCallback(final String functionName) {
        return functionCallbackMap.containsKey(JsonFunctionRunner.transformFunctionName(functionName));
    }

    /**
     * Register a function callback
     *
     * @param functionName     the name of the function
     * @param functionCallback the function callback
     * @return this
     */
    public JsonFunctionRunner registerFunctionCallback(
            final String functionName,
            final FunctionCallback functionCallback
    ) {
        functionCallbackMap.put(JsonFunctionRunner.transformFunctionName(functionName), functionCallback);
        return this;
    }

    /**
     * Run the function with the given parser and function
     *
     * @param parser   the parser
     * @param function the function
     * @return the result of the function
     */
    public JsonElement runFunction(final JsonParser parser, final JsonFunction function) throws TokenTypeExpectedException {
        final FunctionCallback callback = this.getFunctionCallback(function.getFunctionName());
        if (callback == null) {
            throw new RuntimeException("Unknown function: " + function.getFunctionName());
        }
        return callback.run(parser, function);
    }

    /**
     * Copy the JsonFunctionRunner
     *
     * @return the copy of the JsonFunctionRunner
     */
    public JsonFunctionRunner copy() {
        return new JsonFunctionRunner(new HashMap<>(this.functionCallbackMap));
    }

}
