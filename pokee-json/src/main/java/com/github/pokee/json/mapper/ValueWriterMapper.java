package com.github.pokee.json.mapper;

import java.lang.reflect.Field;

/**
 * Interface for mapping objects to string values serializing objects to JSON.
 */
public interface ValueWriterMapper extends Mapper {

    /**
     * Write a value to a string
     *
     * @param writer the writer to write to
     * @param bob    the string builder to write to
     * @param field  the field to write
     * @param value  the value to write
     */
    void writeValue(final JsonWriterMapper writer, final StringBuilder bob, final Field field, final Object value);

}
