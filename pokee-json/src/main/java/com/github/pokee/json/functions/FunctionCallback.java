package com.github.pokee.json.functions;

import com.github.pokee.json.exception.TokenTypeExpectedException;
import com.github.pokee.json.parser.JsonParser;
import com.github.pokee.json.value.JsonElement;
import com.github.pokee.json.value.JsonFunction;

public interface FunctionCallback {

    /**
     * Run the function with the given parser and function
     *
     * @param parser   the parser to use
     * @param function the function to run
     * @return the result of the function
     * @throws TokenTypeExpectedException if the token type is not as expected
     */
    JsonElement run(final JsonParser parser, final JsonFunction function) throws TokenTypeExpectedException;


    /**
     * Asserts that the function has exactly the given number of parameters
     *
     * @param function the function to check
     * @param count    the number of parameters
     */
    default void assertParameterCount(final JsonFunction function, final int count) {
        if (function.getParameterCount() != count) {
            throw new IllegalArgumentException("Function " + function.getFunctionName() + " requires exactly " + count + " parameters");
        }
    }

    /**
     * Asserts that the function has at least the given number of parameters
     *
     * @param function the function to check
     * @param count    the number of parameters
     */
    default void assertMinParameterCount(final JsonFunction function, final int count) {
        if (function.getParameterCount() < count) {
            throw new IllegalArgumentException("Function " + function.getFunctionName() + " requires at least " + count + " parameters");
        }
    }

}
