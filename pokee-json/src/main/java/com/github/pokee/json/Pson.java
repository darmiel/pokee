package com.github.pokee.json;

import com.github.pokee.json.exception.TokenTypeExpectedException;
import com.github.pokee.json.mapper.*;
import com.github.pokee.json.token.JsonParser;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Pson {

    private final JsonWriterMapper jsonWriterMapper;
    private final JsonReaderMapper jsonReaderMapper;

    Pson(
            final boolean serializeNulls,
            final String prettyPrintIndent,
            final Map<Class<?>, List<FieldMapper<ValueWriterMapper>>> valueWriterMapperMap,
            final Map<Class<?>, List<FieldMapper<ValueReaderMapper>>> valueReaderMapperMap
    ) {
        this.jsonWriterMapper = new JsonWriterMapper(
                serializeNulls,
                prettyPrintIndent,
                valueWriterMapperMap
        );
        this.jsonReaderMapper = new JsonReaderMapper(
                valueReaderMapperMap
        );
    }


    /**
     * Create a new PsonBuilder
     *
     * @return the builder
     */
    public static PsonBuilder create() {
        return new PsonBuilder();
    }

    /**
     * Create a new PsonBuilder with default settings, which include:
     * - pretty print
     * - serialize nulls
     *
     * @return the builder
     */
    public static PsonBuilder createWithDefaults() {
        return Pson.create()
                .prettyPrint()
                .serializeNulls();
    }

    /**
     * Marshal an object to a JSON string
     *
     * @param object the object to marshal
     * @return the JSON string
     */
    public String marshal(final Object object) {
        final StringBuilder bob = new StringBuilder();
        this.jsonWriterMapper.write(bob, null, object, 0);
        return bob.toString();
    }

    /**
     * Marshal an object to a JSON string
     *
     * @param bob    the string builder to write to
     * @param object the object to marshal
     */
    public void marshal(final StringBuilder bob, final Object object) {
        this.jsonWriterMapper.write(bob, null, object, 0);
    }

    /**
     * Unmarshal a JSON string to an object
     *
     * @param json  the JSON string
     * @param clazz the class of the object
     * @param <T>   the type of the object
     * @return the object
     * @throws IllegalStateException if the object could not be unmarshalled
     */
    public <T> T unmarshalObject(final String json, final Class<T> clazz) {
        final JsonParser jsonParser = new JsonParser(json);
        try {
            return this.jsonReaderMapper.readObject(jsonParser.parse().asObject(), clazz);
        } catch (TokenTypeExpectedException | ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to unmarshal object", e);
        }
    }

    /**
     * Unmarshal a JSON string to a map
     *
     * @param json the JSON string
     * @return the map
     * @throws IllegalStateException if the map could not be unmarshalled
     */
    public <T> T[] unmarshalArray(final String json, final Class<T> clazz) {
        final JsonParser jsonParser = new JsonParser(json);
        try {
            return this.jsonReaderMapper.readArray(jsonParser.parse().asArray(), clazz);
        } catch (TokenTypeExpectedException | ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to unmarshal array", e);
        }
    }

    /**
     * Unmarshal a JSON string to a list
     *
     * @param json  the JSON string
     * @param clazz the class of the list
     * @return the list
     * @throws IllegalStateException if the list could not be unmarshalled
     */
    public <T> List<T> unmarshalList(final String json, final Class<T> clazz) {
        final JsonParser jsonParser = new JsonParser(json);
        try {
            return this.jsonReaderMapper.readArrayAsList(jsonParser.parse().asArray(), clazz);
        } catch (TokenTypeExpectedException | ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to unmarshal list", e);
        }
    }

}
