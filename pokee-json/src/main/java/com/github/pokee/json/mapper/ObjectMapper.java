package com.github.pokee.json.mapper;

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

public class ObjectMapper {

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

    public static <T> T mapJsonObject(
            final JsonObject object,
            final Class<T> clazz
    ) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
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
                throw new IllegalArgumentException("Missing required field: " + key);
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

    public static <T> T parse(
            final String json,
            final Class<T> clazz
    ) throws TokenTypeExpectedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final JsonParser parser = new JsonParser(json);
        final JsonElement element = parser.parse();
        if (element.isObject()) {
            return mapJsonObject(element.asObject(), clazz);
        }
        throw new IllegalArgumentException("Invalid JSON");
    }

}
