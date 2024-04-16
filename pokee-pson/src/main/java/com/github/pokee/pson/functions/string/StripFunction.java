package com.github.pokee.pson.functions.string;

import com.github.pokee.pson.functions.FunctionCallback;
import com.github.pokee.pson.parser.JsonParser;
import com.github.pokee.pson.value.JsonElement;
import com.github.pokee.pson.value.JsonFunction;
import com.github.pokee.pson.value.JsonObject;
import com.github.pokee.pson.value.JsonPrimitive;

public class StripFunction implements FunctionCallback {

    public static final String NAME = "strip";
    public static final StripFunction INSTANCE = new StripFunction();

    @Override
    public JsonElement run(final JsonParser parser, final JsonFunction function) {
        assertMinParameterCount(function, 1);

        final String value = function.getParameter(0).asPrimitive().asString();
        final JsonObject options = function.getOptions();

        final boolean left = options.has("left") && options.get("left").asPrimitive().asBoolean();
        final boolean right = options.has("right") && options.get("right").asPrimitive().asBoolean();

        if (left && !right) {
            return JsonPrimitive.fromString(value.stripLeading());
        }
        if (!left && right) {
            return JsonPrimitive.fromString(value.stripTrailing());
        }
        return JsonPrimitive.fromString(value.strip());
    }

}
