package com.github.pokee.common.fielder;

import java.util.HashMap;
import java.util.Map;

public class MapFielder implements Fielder {

    private final Map<String, Object> fields = new HashMap<>();

    /**
     * Associates the specified value with the specified key in this map
     *
     * @param key   the key
     * @param value the value
     */
    public void put(final String key, final Object value) {
        this.fields.put(key, value);
    }

    @Override
    public boolean hasField(String name) {
        return this.fields.containsKey(name);
    }

    @Override
    public Object getField(String name) {
        return this.fields.get(name);
    }

    @Override
    public String[] getFields() {
        return fields.keySet().toArray(new String[0]);
    }

    @Override
    public String toString() {
        return this.fields.toString();
    }
}
