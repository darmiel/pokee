package com.github.pokee.json.parser;

import com.github.pokee.json.exception.TokenTypeExpectedException;
import com.github.pokee.json.functions.FunctionCallback;
import com.github.pokee.json.functions.load.EnvironmentAsJsonFunction;
import com.github.pokee.json.functions.load.EnvironmentFunction;
import com.github.pokee.json.functions.load.FileAsJsonFunction;
import com.github.pokee.json.functions.load.FileFunction;
import com.github.pokee.json.functions.logic.BooleanFunctions;
import com.github.pokee.json.functions.logic.DefaultFunction;
import com.github.pokee.json.functions.logic.EqualsFunctions;
import com.github.pokee.json.functions.logic.IfFunction;
import com.github.pokee.json.functions.string.StripFunction;
import com.github.pokee.json.functions.string.TransformerFunctions;
import com.github.pokee.json.value.JsonElement;
import com.github.pokee.json.value.JsonFunction;
import com.github.pokee.json.value.JsonObject;
import com.github.pokee.json.value.JsonPrimitive;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to run a function
 */
public class JsonFunctionRunner {

    public static final String DEFAULT_OPTION = "default";
    public static final String OPTIONAL_OPTION = "optional";

    /**
     * If the function has a default parameter, return it, otherwise throw an exception
     *
     * @param options      the options
     * @param errorMessage the error message
     * @return the default parameter
     */
    public static JsonElement getOrDefault(final JsonObject options, final String errorMessage) {
        if (options.has(DEFAULT_OPTION)) {
            return options.get(DEFAULT_OPTION);
        }
        if (options.has(OPTIONAL_OPTION) && options.get(OPTIONAL_OPTION).asPrimitive().asBoolean()) {
            return JsonPrimitive.fromNull();
        }
        throw new IllegalArgumentException(errorMessage);
    }

    /**
     * Returns a JsonFunctionRunner with the default functions
     *
     * @return the JsonFunctionRunner
     */
    public static JsonFunctionRunner defaultFunctionRunner() {
        return new JsonFunctionRunner(new HashMap<>())
                // Load functions
                .registerFunctionCallback(EnvironmentFunction.NAME, EnvironmentFunction.INSTANCE)
                .registerFunctionCallback(EnvironmentAsJsonFunction.NAME, EnvironmentAsJsonFunction.INSTANCE)
                .registerFunctionCallback(FileFunction.NAME, FileFunction.INSTANCE)
                .registerFunctionCallback(FileAsJsonFunction.NAME, FileAsJsonFunction.INSTANCE)
                // String functions
                .registerFunctionCallback(StripFunction.NAME, StripFunction.INSTANCE)
                .registerFunctionCallback(TransformerFunctions.ToUpper.NAME, TransformerFunctions.ToUpper.INSTANCE)
                .registerFunctionCallback(TransformerFunctions.ToLower.NAME, TransformerFunctions.ToLower.INSTANCE)
                // Logic functions
                .registerFunctionCallback(IfFunction.NAME, IfFunction.INSTANCE)
                .registerFunctionCallback(BooleanFunctions.IsTrueFunction.NAME, BooleanFunctions.IsTrueFunction.INSTANCE)
                .registerFunctionCallback(BooleanFunctions.IsFalseFunction.NAME, BooleanFunctions.IsFalseFunction.INSTANCE)
                .registerFunctionCallback(EqualsFunctions.EqualsFunction.NAME, EqualsFunctions.EqualsFunction.INSTANCE)
                .registerFunctionCallback(EqualsFunctions.GreaterThan.NAME, EqualsFunctions.GreaterThan.INSTANCE)
                .registerFunctionCallback(EqualsFunctions.GreaterThanOrEquals.NAME, EqualsFunctions.GreaterThanOrEquals.INSTANCE)
                .registerFunctionCallback(EqualsFunctions.LessThan.NAME, EqualsFunctions.LessThan.INSTANCE)
                .registerFunctionCallback(EqualsFunctions.LessThanOrEquals.NAME, EqualsFunctions.LessThanOrEquals.INSTANCE)
                .registerFunctionCallback(DefaultFunction.NAME, DefaultFunction.INSTANCE)
                ;
    }

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

}
