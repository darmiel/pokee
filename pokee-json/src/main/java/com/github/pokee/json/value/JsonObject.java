package com.github.pokee.json.value;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a JSON object
 */
public class JsonObject implements JsonElement {

    private final Map<String, JsonElement> entries;

    public JsonObject() {
        this.entries = new HashMap<>();
    }

    /**
     * Put a new key-value pair into the object
     *
     * @param key   The key
     * @param value The value
     */
    public void put(final String key, final JsonElement value) {
        this.entries.put(key, value);
    }

    /**
     * Get the value for a given key
     *
     * @param key The key
     * @return The value if it exists, null otherwise
     */
    public JsonElement get(final String key) {
        return this.entries.get(key);
    }

    /**
     * Check if the object contains a given key
     *
     * @param key The key
     * @return true if the key exists, false otherwise
     */
    public boolean has(final String key) {
        return this.entries.containsKey(key);
    }

    @Override
    public String toString() {
        return "JsonObject{" +
                "entries=" + entries +
                '}';
    }

}
