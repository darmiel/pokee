package com.github.pokee.pson.value;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a JSON array
 */
public class JsonArray implements JsonElement, ToStringDepth {

    private final List<JsonElement> elements = new ArrayList<>();

    /**
     * Adds a new element to the array
     *
     * @param element The element to add
     */
    public void add(final JsonElement element) {
        this.elements.add(element);
    }

    /**
     * Returns the element at the specified index
     *
     * @param index The index of the element
     * @return The element at the specified index
     */
    public JsonElement get(final int index) {
        return this.elements.get(index);
    }

    /**
     * Returns the number of elements in the array
     *
     * @return The number of elements in the array
     */
    public int size() {
        return this.elements.size();
    }

    @Override
    public String toString(int depth) {
        final StringBuilder bob = new StringBuilder();
        bob.append("JsonArray [\n");
        for (JsonElement entry : this.elements) {
            final String entryValue = entry instanceof final ToStringDepth toStringDepth
                    ? toStringDepth.toString(depth + 1)
                    : Objects.toString(entry);
            bob.append("  ".repeat(depth + 1)).append(entryValue).append(",\n");
        }
        bob.append("  ".repeat(depth)).append("]");
        return bob.toString();
    }

    @Override
    public String toString() {
        return "JsonArray{" +
                "elements=" + elements +
                '}';
    }
}
