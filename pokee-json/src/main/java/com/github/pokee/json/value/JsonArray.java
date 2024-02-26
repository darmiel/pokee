package com.github.pokee.json.value;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a JSON array
 */
public class JsonArray implements JsonElement {

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
    public String toString() {
        return "JsonArray{" +
                "elements=" + elements +
                '}';
    }
}
