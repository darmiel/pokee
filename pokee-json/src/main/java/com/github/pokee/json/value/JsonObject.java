package com.github.pokee.json.value;

import java.util.HashMap;
import java.util.Map;

public class JsonObject implements JsonElement {

    private final Map<String, JsonElement> entries;

    public JsonObject() {
        this.entries = new HashMap<>();
    }

    public void put(final String key, final JsonElement value) {
        this.entries.put(key, value);
    }

    public JsonElement get(final String key) {
        return this.entries.get(key);
    }

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
