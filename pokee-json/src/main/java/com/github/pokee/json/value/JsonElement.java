package com.github.pokee.json.value;

public interface JsonElement {

    default boolean isObject() {
        return this instanceof JsonObject;
    }

    default boolean isArray() {
        return this instanceof JsonArray;
    }

    default boolean isPrimitive() {
        return this instanceof JsonPrimitive;
    }

    default JsonObject asObject() {
        return (JsonObject) this;
    }

    default JsonArray asArray() {
        return (JsonArray) this;
    }

    default JsonPrimitive asPrimitive() {
        return (JsonPrimitive) this;
    }

}
