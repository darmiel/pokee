package com.github.pokee.json.value;

import java.util.ArrayList;
import java.util.List;

public class JsonArray implements JsonElement {

    private final List<JsonElement> elements = new ArrayList<>();

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
