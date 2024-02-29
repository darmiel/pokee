package com.github.pokee.json;

import com.github.pokee.json.exception.TokenTypeExpectedException;
import com.github.pokee.json.mapper.*;
import com.github.pokee.json.parser.JsonFunctionRunner;
import com.github.pokee.json.parser.JsonParser;
import com.github.pokee.json.value.JsonArray;
import com.github.pokee.json.value.JsonElement;
import com.github.pokee.json.value.JsonObject;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Pson {

    private final JsonWriterMapper jsonWriterMapper;
    private final JsonReaderMapper jsonReaderMapper;

    // functions
    private final JsonFunctionRunner jsonFunctionRunner;
    private final boolean expandFunctions;

    Pson(
            final boolean serializeNulls,
            final String prettyPrintIndent,
            final Map<Class<?>, List<FieldMapper<ValueWriterMapper>>> valueWriterMapperMap,
            final Map<Class<?>, List<FieldMapper<ValueReaderMapper>>> valueReaderMapperMap,
            final JsonFunctionRunner jsonFunctionRunner,
            final boolean expandFunctions
    ) {
        this.jsonWriterMapper = new JsonWriterMapper(
                serializeNulls,
                prettyPrintIndent,
                valueWriterMapperMap
        );
        this.jsonReaderMapper = new JsonReaderMapper(
                valueReaderMapperMap
        );
        this.jsonFunctionRunner = jsonFunctionRunner;
        this.expandFunctions = expandFunctions;
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

    private JsonParser createParser(final String json) {
        return new JsonParser(json, this.expandFunctions, this.jsonFunctionRunner);
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
     * Unmarshal a JSON string to a JsonElement
     *
     * @param json the JSON string
     * @return the JsonElement
     * @throws IllegalStateException if the JSON could not be unmarshalled
     */
    public JsonElement unmarshal(final String json) {
        final JsonParser parser = this.createParser(json);
        try {
            return parser.parse();
        } catch (TokenTypeExpectedException e) {
            throw new IllegalStateException("Failed to unmarshal JSON", e);
        }
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
        try {
            final JsonObject jsonObject = this.unmarshal(json).asObject();
            return this.jsonReaderMapper.readObject(jsonObject, clazz);
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
        try {
            final JsonArray jsonArray = this.unmarshal(json).asArray();
            return this.jsonReaderMapper.readArray(jsonArray, clazz);
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
        try {
            final JsonArray jsonArray = this.unmarshal(json).asArray();
            return this.jsonReaderMapper.readArrayAsList(jsonArray, clazz);
        } catch (TokenTypeExpectedException | ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to unmarshal list", e);
        }
    }

}
