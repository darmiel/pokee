package com.github.pokee.json.mapper;

import java.lang.reflect.Field;

/**
 * Interface for mapping objects to string values serializing objects to JSON.
 */
public interface ValueWriterMapper extends Mapper {

    void writeValue(final JsonMapper writer, final StringBuilder bob, final Field field, final Object value);

}
