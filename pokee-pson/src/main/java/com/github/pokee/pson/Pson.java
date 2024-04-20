package com.github.pokee.pson;

import com.github.pokee.pson.exception.TokenTypeExpectedException;
import com.github.pokee.pson.mapper.*;
import com.github.pokee.pson.mapper.annotations.PsonSource;
import com.github.pokee.pson.parser.JsonFunctionRunner;
import com.github.pokee.pson.parser.JsonParser;
import com.github.pokee.pson.value.JsonArray;
import com.github.pokee.pson.value.JsonElement;
import com.github.pokee.pson.value.JsonObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
     * Unmarshal a file to an object
     *
     * @param fileName the name of the file
     * @param clazz    the class of the object
     * @param <T>      the type of the object
     * @return the object
     * @throws IllegalStateException if the object could not be unmarshalled
     * @throws IOException           if the file could not be read
     */
    public <T> T unmarshalObjectFromFile(final String fileName, final Class<T> clazz) throws IOException {
        return this.unmarshalObjectFromFile(new File(fileName), clazz);
    }

    /**
     * Unmarshal a file to an object
     *
     * @param file  the file
     * @param clazz the class of the object
     * @param <T>   the type of the object
     * @return the object
     * @throws IllegalStateException if the object could not be unmarshalled
     * @throws IOException           if the file could not be read
     */
    public <T> T unmarshalObjectFromFile(final File file, final Class<T> clazz) throws IOException {
        return this.unmarshalObjectFromFile(file.toPath(), clazz);
    }

    /**
     * Unmarshal a file to an object
     *
     * @param path  the path of the file
     * @param clazz the class of the object
     * @param <T>   the type of the object
     * @return the object
     * @throws IllegalStateException if the object could not be unmarshalled
     * @throws IOException           if the file could not be read
     */
    public <T> T unmarshalObjectFromFile(final Path path, final Class<T> clazz) throws IOException {
        return this.unmarshalObject(String.join("\n", Files.readAllLines(path)), clazz);
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
     * Unmarshal a JSON string to an object
     *
     * @param clazz the class of the object
     * @return the unmarshalled object
     * @throws IllegalArgumentException if the class is not annotated with @PsonSource
     */
    public <T> T unmarshalObject(final Class<T> clazz) throws IOException {
        if (!clazz.isAnnotationPresent(PsonSource.class)) {
            throw new IllegalArgumentException("Class must be annotated with @PsonSource");
        }
        final PsonSource source = clazz.getAnnotation(PsonSource.class);
        if (!source.raw().isBlank()) {
            return this.unmarshalObject(source.raw(), clazz);
        }
        if (!source.file().isBlank()) {
            return this.unmarshalObjectFromFile(source.file(), clazz);
        }
        throw new IllegalArgumentException("Class must have either raw or file set in @PsonSource");
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
     * Unmarshal a JSON file to an array of objects of a specified type.
     *
     * @param file  the file containing the JSON data
     * @param clazz the class of the array elements
     * @param <T>   the type of the array elements
     * @return the array of objects of type T
     * @throws IOException           if the file could not be read
     * @throws IllegalStateException if the JSON could not be unmarshalled
     */
    public <T> T[] unmarshalArrayFromFile(final File file, final Class<T> clazz) throws IOException {
        final String json = new String(Files.readAllBytes(file.toPath()));
        return this.unmarshalArray(json, clazz);
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

    /**
     * Unmarshal a JSON file to a list of objects of a specified type.
     *
     * @param file  the file containing the JSON data
     * @param clazz the class of the list elements
     * @param <T>   the type of the list elements
     * @return the list of objects of type T
     * @throws IOException           if the file could not be read
     * @throws IllegalStateException if the JSON could not be unmarshalled
     */
    public <T> List<T> unmarshalListFromFile(final File file, final Class<T> clazz) throws IOException {
        final String json = new String(Files.readAllBytes(file.toPath()));
        return this.unmarshalList(json, clazz);
    }


}
