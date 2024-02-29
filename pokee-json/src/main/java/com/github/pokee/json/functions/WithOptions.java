package com.github.pokee.json.functions;

import com.github.pokee.json.value.JsonElement;
import com.github.pokee.json.value.JsonObject;

public interface WithOptions<T, E extends Exception> {

    JsonElement run(final JsonObject options, final T value) throws E;

}
