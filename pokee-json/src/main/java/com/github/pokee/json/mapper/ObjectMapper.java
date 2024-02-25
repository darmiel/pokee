package com.github.pokee.json.mapper;

import com.github.pokee.json.exception.MissingRequiredFieldException;
import com.github.pokee.json.exception.TokenTypeExpectedException;
import com.github.pokee.json.mapper.annotations.Ignored;
import com.github.pokee.json.mapper.annotations.Optional;
import com.github.pokee.json.mapper.annotations.Property;
import com.github.pokee.json.token.JsonParser;
import com.github.pokee.json.value.JsonElement;
import com.github.pokee.json.value.JsonObject;
import com.github.pokee.json.value.JsonPrimitive;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Maps a JSON object to a Java object
 */
public class ObjectMapper {

    /**
     * Supported converters for primitive types
     */
    public static final Map<Class<?>, Function<String, Object>> CONVERTERS = new HashMap<>() {{
        put(String.class, s -> s);
        put(int.class, Integer::parseInt);
        put(Integer.class, Integer::parseInt);
        put(long.class, Long::parseLong);
        put(Long.class, Long::parseLong);
        put(double.class, Double::parseDouble);
        put(Double.class, Double::parseDouble);
        put(float.class, Float::parseFloat);
        put(Float.class, Float::parseFloat);
        put(boolean.class, Boolean::parseBoolean);
        put(Boolean.class, Boolean::parseBoolean);
    }};

    /**
     * Maps a JsonObject to a Java object
     *
     * @param object The JsonObject to map
     * @param clazz  The class to map to
     * @param <T>    The type of the object
     * @return The mapped object
     * @throws NoSuchMethodException     If the no-args constructor is missing
     * @throws InvocationTargetException If the constructor throws an exception
     * @throws InstantiationException    If the class is abstract or an interface
     * @throws IllegalAccessException    If the class or its no-args constructor is not accessible
     */
    public static <T> T mapJsonObject(
            final JsonObject object,
            final Class<T> clazz
    ) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, MissingRequiredFieldException {
        // we require a no-args constructor
        final Constructor<T> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        final T instance = constructor.newInstance();

        for (final Field declaredField : clazz.getDeclaredFields()) {
            // ignore field if annotated with @Ignored
            if (declaredField.isAnnotationPresent(Ignored.class)) {
                continue;
            }

            // get key from annotation or field name
            final String key = declaredField.isAnnotationPresent(Property.class)
                    ? declaredField.getAnnotation(Property.class).value()
                    : declaredField.getName();

            // get value from JsonObject
            if (!object.has(key)) {
                if (declaredField.isAnnotationPresent(Optional.class)) {
                    continue;
                }
                throw new MissingRequiredFieldException(key);
            }

            final Object fieldValue;
            final JsonElement value = object.get(key);

            if (value.isObject()) {
                fieldValue = mapJsonObject(value.asObject(), declaredField.getType());
            } else if (value.isPrimitive()) {
                final JsonPrimitive primitive = value.asPrimitive();
                if (primitive.isNull()) {
                    fieldValue = null;
                } else if (primitive.isString()) {
                    fieldValue = primitive.asString();
                } else {
                    final Function<String, Object> converter = CONVERTERS.get(declaredField.getType());
                    if (converter == null) {
                        throw new IllegalArgumentException("Primitive not supported");
                    }
                    fieldValue = converter.apply(value.asPrimitive().getRawValue());
                }
            } else if (value.isArray()) {
                throw new IllegalArgumentException("Array not supported");
            } else {
                throw new IllegalArgumentException("Unknown type");
            }

            declaredField.setAccessible(true);
            declaredField.set(instance, fieldValue);
        }

        return instance;
    }

    /**
     * Parses a JSON string to a Java object
     *
     * @param json  The JSON string
     * @param clazz The class to map to
     * @param <T>   The type of the object
     * @return The mapped object
     * @throws TokenTypeExpectedException    If the JSON is invalid
     * @throws InvocationTargetException     If the constructor throws an exception
     * @throws NoSuchMethodException         If the no-args constructor is missing
     * @throws InstantiationException        If the class is abstract or an interface
     * @throws IllegalAccessException        If the class or its no-args constructor is not accessible
     * @throws MissingRequiredFieldException If a required field is missing
     */
    public static <T> T parse(
            final String json,
            final Class<T> clazz
    ) throws TokenTypeExpectedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, MissingRequiredFieldException {
        final JsonParser parser = new JsonParser(json);
        final JsonElement element = parser.parse();
        if (element.isObject()) {
            return mapJsonObject(element.asObject(), clazz);
        }
        throw new IllegalArgumentException("Invalid JSON");
    }

}
