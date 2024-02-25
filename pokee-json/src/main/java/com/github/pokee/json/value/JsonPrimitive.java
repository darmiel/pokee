package com.github.pokee.json.value;

/**
 * Represents a JSON primitive value.
 */
public class JsonPrimitive implements JsonElement {

    private final String rawValue;

    public JsonPrimitive(final String rawValue) {
        this.rawValue = rawValue;
    }

    /**
     * Checks if the raw value is a string
     *
     * @return true if the raw value is a string, false otherwise
     */
    public boolean isString() {
        return this.rawValue.startsWith("\"") && this.rawValue.endsWith("\"");
    }

    /**
     * Returns the raw value as a string and removes the quotes
     *
     * @return the raw value as a string
     */
    public String asString() {
        return this.rawValue.substring(1, this.rawValue.length() - 1);
    }

    /**
     * Checks if the raw value is a double
     *
     * @return true if the raw value is a double, false otherwise
     */
    public boolean isDouble() {
        try {
            Double.parseDouble(this.rawValue);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Parses the raw value as a double
     *
     * @return the raw value as a double
     */
    public double asDouble() {
        return Double.parseDouble(this.rawValue);
    }

    /**
     * Checks if the raw value is an integer
     *
     * @return true if the raw value is an integer, false otherwise
     */
    public boolean isInteger() {
        try {
            Integer.parseInt(this.rawValue);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Parses the raw value as an integer
     *
     * @return the raw value as an integer
     */
    public int asInteger() {
        return Integer.parseInt(this.rawValue);
    }

    /**
     * Checks if the raw value is null
     *
     * @return true if the raw value is null, false otherwise
     */
    public boolean isNull() {
        return "null".equals(this.rawValue);
    }

    /**
     * Returns the raw value
     *
     * @return the raw value
     */
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
