package com.github.pokee.pson.functions.load;

import com.github.pokee.pson.exception.TokenTypeExpectedException;
import com.github.pokee.pson.functions.FunctionCallback;
import com.github.pokee.pson.functions.WithOptions;
import com.github.pokee.pson.parser.JsonFunctionRunner;
import com.github.pokee.pson.parser.JsonParser;
import com.github.pokee.pson.value.JsonElement;
import com.github.pokee.pson.value.JsonFunction;
import com.github.pokee.pson.value.JsonObject;
import com.github.pokee.pson.value.JsonPrimitive;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This function returns the contents of a file
 * <p>
 * The first parameter is the path to the file
 * The second parameter is an object with the following optional fields:
 * - optional: boolean (default: false)
 * - default: any (default: null)
 * - json: boolean (default: false)
 * If the file is not found, and optional is true, it returns null
 * If the file is not found, and optional is false, it throws an exception
 * If the file is found, and json is true, it parses the contents as JSON
 * If the file is found, and json is false, it returns the contents as a string
 * </p>
 */
public class FileFunction implements FunctionCallback {

    public static final String NAME = "file";
    public static final FileFunction INSTANCE = new FileFunction();
    public static final String JSON_OPTION = "json";

    public static JsonElement run(
            final JsonFunction function,
            final WithOptions<String, TokenTypeExpectedException> withOptions
    ) throws TokenTypeExpectedException {
        final String path = function.getParameter(0).asPrimitive().asString();
        final JsonObject options = function.getOptions();

        // read contents from file
        String contents;
        try {
            contents = String.join("\n", Files.readAllLines(Paths.get(path)));
        } catch (IOException e) {
            contents = null;
        }

        if (contents == null) {
            return JsonFunctionRunner.getOrDefault(options, "File not found: " + path);
        }

        return withOptions.run(options, contents);
    }

    @Override
    public JsonElement run(final JsonParser parser, final JsonFunction function) throws TokenTypeExpectedException {
        assertMinParameterCount(function, 1);
        return FileFunction.run(function, (options, contents) -> {
            if (options.has(JSON_OPTION) && options.get(JSON_OPTION).asPrimitive().asBoolean()) {
                return parser.copyConfiguration(contents).parse();
            }
            return JsonPrimitive.fromString(contents);
        });
    }

}
