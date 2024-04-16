package com.github.pokee.pson.mapper.mappers;

import com.github.pokee.pson.mapper.JsonWriterMapper;
import com.github.pokee.pson.mapper.ValueReaderWriterMapper;
import com.github.pokee.pson.value.JsonElement;

import java.lang.reflect.Field;
import java.util.UUID;

public class UUIDMapper implements ValueReaderWriterMapper {

    public static final UUIDMapper INSTANCE = new UUIDMapper();

    @Override
    public Object mapValue(JsonElement element, Field field) {
        return UUID.fromString(element.asPrimitive().asString());
    }

    @Override
    public void writeValue(JsonWriterMapper writer, StringBuilder bob, Field field, Object value) {
        writer.writeString(bob, value.toString());
    }

}
