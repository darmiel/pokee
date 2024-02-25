package com.github.pokee.json.value;

public class JsonPrimitive implements JsonElement {

    private final String rawValue;

    public JsonPrimitive(final String rawValue) {
        this.rawValue = rawValue;
    }

    public boolean isString() {
        return this.rawValue.startsWith("\"") && this.rawValue.endsWith("\"");
    }

    public String asString() {
        return this.rawValue.substring(1, this.rawValue.length() - 1);
    }

    public boolean isDouble() {
        try {
            Double.parseDouble(this.rawValue);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public double asDouble() {
        return Double.parseDouble(this.rawValue);
    }

    public boolean isInteger() {
        try {
            Integer.parseInt(this.rawValue);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public int asInteger() {
        return Integer.parseInt(this.rawValue);
    }

    public boolean isNull() {
        return "null".equals(this.rawValue);
    }

    public String getRawValue() {
        return rawValue;
    }

    @Override
    public String toString() {
        return "JsonPrimitive{" +
                "rawValue='" + rawValue + '\'' +
                '}';
    }

}
