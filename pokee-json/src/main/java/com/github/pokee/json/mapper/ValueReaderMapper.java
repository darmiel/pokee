package com.github.pokee.json.mapper;

import com.github.pokee.json.value.JsonElement;

import java.lang.reflect.Field;

/**
 * Interface for mapping string values to objects used for deserializing JSON to objects.
 */
public interface ValueReaderMapper extends Mapper {

    Object mapValue(final JsonElement element, final Field field, final String value);

}
