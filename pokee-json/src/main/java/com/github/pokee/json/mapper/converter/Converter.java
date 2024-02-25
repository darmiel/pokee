package com.github.pokee.json.mapper.converter;

public interface Converter <T> {

    T convert(final String value);

}
