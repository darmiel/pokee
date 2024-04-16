package com.github.pokee.pson.functions.load;

import com.github.pokee.pson.exception.TokenTypeExpectedException;
import com.github.pokee.pson.functions.FunctionCallback;
import com.github.pokee.pson.parser.JsonParser;
import com.github.pokee.pson.value.JsonElement;
import com.github.pokee.pson.value.JsonFunction;

/**
 * This function returns the contents of a file
 * <p>
 * The first parameter is the path to the file
 * The second parameter is an object with the following optional fields:
 * - optional: boolean (default: false)
 * - default: any (default: null)
 * If the file is not found, and optional is true, it returns null
 * If the file is not found, and optional is false, it throws an exception
 * </p>
 */
public class FileAsJsonFunction implements FunctionCallback {

    public static final String NAME = "json-file";
    public static final FileAsJsonFunction INSTANCE = new FileAsJsonFunction();

    @Override
    public JsonElement run(final JsonParser parser, final JsonFunction function) throws TokenTypeExpectedException {
        assertMinParameterCount(function, 1);
        return FileFunction.run(function, (options, contents) -> parser.copyConfiguration(contents).parse());
    }

}
