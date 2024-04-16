package com.github.pokee.pson.value;

import com.github.pokee.pson.mapper.JsonReaderMapper;

/**
 * Represents a JSON primitive value.
 *
 * @noinspection unused
 */
public class JsonPrimitive implements JsonElement {

    public static final String NULL = "null";
    public static final String TRUE = "true";
    public static final String FALSE = "false";

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
        if (!this.isString()) {
            throw new IllegalStateException("The raw value is not a string");
        }
        return JsonReaderMapper.unescapeString(this.rawValue.substring(1, this.rawValue.length() - 1));
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
        if (!this.isDouble()) {
            throw new IllegalStateException("The raw value is not a double");
        }
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
        if (!this.isInteger()) {
            throw new IllegalStateException("The raw value is not an integer");
        }
        return Integer.parseInt(this.rawValue);
    }

    /**
     * Checks if the raw value is null
     *
     * @return true if the raw value is null, false otherwise
     */
    public boolean isNull() {
        return JsonPrimitive.NULL.equals(this.rawValue);
    }

    /**
     * Checks if the raw value is a boolean
     *
     * @return true if the raw value is a boolean, false otherwise
     */
    public boolean isBoolean() {
        return JsonPrimitive.TRUE.equals(this.rawValue) || JsonPrimitive.FALSE.equals(this.rawValue);
    }

    /**
     * Parses the raw value as a boolean
     *
     * @return the raw value as a boolean
     */
    public boolean asBoolean() {
        if (!this.isBoolean()) {
            throw new IllegalStateException("The raw value is not a boolean");
        }
        return JsonPrimitive.TRUE.equals(this.rawValue);
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
        if (this.isString()) {
            return "str(" + this.asString() + ")";
        }
        if (this.isDouble()) {
            return "num(" + this.asDouble() + ")";
        }
        if (this.isInteger()) {
            return "int(" + this.asInteger() + ")";
        }
        if (this.isNull()) {
            return "null()";
        }
        if (this.isBoolean()) {
            return "bool(" + this.asBoolean() + ")";
        }
        return "raw-primitive(" + this.rawValue + ")";
    }

    public static JsonPrimitive fromString(final String value) {
        return new JsonPrimitive("\"" + value + "\"");
    }

    public static JsonPrimitive fromBool(final boolean value) {
        return new JsonPrimitive(value ? JsonPrimitive.TRUE : JsonPrimitive.FALSE);
    }

    public static JsonPrimitive fromNumber(final double value) {
        return new JsonPrimitive(String.valueOf(value));
    }

    public static JsonPrimitive fromNumber(final int value) {
        return new JsonPrimitive(String.valueOf(value));
    }

    public static JsonPrimitive fromNull() {
        return new JsonPrimitive(JsonPrimitive.NULL);
    }

}
