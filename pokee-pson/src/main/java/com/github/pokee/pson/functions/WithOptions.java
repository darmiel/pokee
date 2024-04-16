package com.github.pokee.pson.functions;

import com.github.pokee.pson.value.JsonElement;
import com.github.pokee.pson.value.JsonObject;

public interface WithOptions<T, E extends Exception> {

    JsonElement run(final JsonObject options, final T value) throws E;

}
