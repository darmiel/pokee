package com.github.pokee.json;

import com.github.pokee.json.mapper.FieldMapper;
import com.github.pokee.json.mapper.JsonMapper;
import com.github.pokee.json.mapper.ValueReaderMapper;
import com.github.pokee.json.mapper.ValueWriterMapper;

import java.util.List;
import java.util.Map;

public class Pson {

    private final JsonMapper jsonMapper;

    Pson(
            final boolean serializeNulls,
            final String prettyPrintIndent,
            final Map<Class<?>, List<FieldMapper<ValueWriterMapper>>> valueWriterMapperMap,
            final Map<Class<?>, ValueReaderMapper> valueReaderMapperMap
    ) {
        this.jsonMapper = new JsonMapper(serializeNulls, prettyPrintIndent, valueWriterMapperMap, valueReaderMapperMap);
    }


    public static PsonBuilder create() {
        return new PsonBuilder();
    }

    public JsonMapper getJsonWriter() {
        return jsonMapper;
    }
}
