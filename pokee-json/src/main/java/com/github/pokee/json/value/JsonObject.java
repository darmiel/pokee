package com.github.pokee.json.value;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a JSON object
 */
public class JsonObject implements JsonElement, ToStringDepth {

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
    public String toString(int depth) {
        final StringBuilder bob = new StringBuilder();
        bob.append("JsonObject {\n");
        for (final Map.Entry<String, JsonElement> entry : this.entries.entrySet()) {
            final String entryValue = entry.getValue() instanceof final ToStringDepth toStringDepth
                    ? toStringDepth.toString(depth + 1)
                    : Objects.toString(entry.getValue());
            bob.append("  ".repeat(depth + 1)).append(entry.getKey()).append(": ").append(entryValue).append(",\n");
        }
        bob.append("  ".repeat(depth)).append("}");
        return bob.toString();
    }

    @Override
    public String toString() {
        return this.toString(0);
    }

}
