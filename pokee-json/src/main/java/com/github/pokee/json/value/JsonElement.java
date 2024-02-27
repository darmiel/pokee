package com.github.pokee.json.value;

/**
 * Represents a generic JSON element.
 */
public interface JsonElement {

    /**
     * Check if this element is a JSON object
     *
     * @return true if this element is a JSON object, false otherwise
     */
    default boolean isObject() {
        return this instanceof JsonObject;
    }

    /**
     * Check if this element is a JSON array
     *
     * @return true if this element is a JSON array, false otherwise
     */
    default boolean isArray() {
        return this instanceof JsonArray;
    }

    /**
     * Check if this element is a JSON primitive
     *
     * @return true if this element is a JSON primitive, false otherwise
     */
    default boolean isPrimitive() {
        return this instanceof JsonPrimitive;
    }

    /**
     * Cast this element to a JSON object
     *
     * @return this element as a JSON object
     */
    default JsonObject asObject() {
        if (!this.isObject()) {
            throw new IllegalStateException("This element is not a JSON object");
        }
        return (JsonObject) this;
    }

    /**
     * Cast this element to a JSON array
     *
     * @return this element as a JSON array
     */
    default JsonArray asArray() {
        if (!this.isArray()) {
            throw new IllegalStateException("This element is not a JSON array");
        }
        return (JsonArray) this;
    }

    /**
     * Cast this element to a JSON primitive
     *
     * @return this element as a JSON primitive
     */
    default JsonPrimitive asPrimitive() {
        if (!this.isPrimitive()) {
            throw new IllegalStateException("This element is not a JSON primitive");
        }
        return (JsonPrimitive) this;
    }

}
