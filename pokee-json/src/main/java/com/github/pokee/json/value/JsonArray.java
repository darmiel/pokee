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

    @Override
    public String toString() {
        return "JsonArray{" +
                "elements=" + elements +
                '}';
    }
}
