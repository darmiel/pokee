package com.github.pokee.pson.mapper;

import com.github.pokee.pson.mapper.annotations.JsonMappper;
import com.github.pokee.pson.mapper.annotations.JsonProperty;
import com.github.pokee.pson.value.JsonPrimitive;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

public class JsonWriterMapper {

    private final String prettyPrintIndent;
    private final boolean serializeNulls;
    private final Map<Class<?>, List<FieldMapper<ValueWriterMapper>>> valueWriterMapperMap;
    private final Map<Class<? extends ValueWriterMapper>, ValueWriterMapper> instantiatedValueWriterMapperMap;

    public JsonWriterMapper(
            final boolean serializeNulls,
            final String prettyPrintIndent,
            final Map<Class<?>, List<FieldMapper<ValueWriterMapper>>> valueWriterMapperMap
    ) {
        this.serializeNulls = serializeNulls;
        this.prettyPrintIndent = prettyPrintIndent;

        this.valueWriterMapperMap = valueWriterMapperMap;
        this.instantiatedValueWriterMapperMap = new HashMap<>();
    }

    /**
     * Escape a string
     * <p>
     * This method escapes the following characters:
     *    <ul>
     *        <li>backspace</li>
     *        <li>form feed</li>
     *        <li>newline</li>
     *        <li>carriage return</li>
     *        <li>tab</li>
     *        <li>backslash</li>
     *        <li>double quote</li>
     * </p>
     *
     * @param input the input string
     * @return the escaped string
     */
    public static String escapeString(final String input) {
        final StringBuilder bob = new StringBuilder();
        for (final char character : input.toCharArray()) {
            final String escape = JsonMapperUtil.ESCAPE_MAP.get(character);
            if (escape != null) {
                bob.append(escape);
            } else {
                bob.append(character);
            }
        }
        return bob.toString();
    }

    /**
     * Pretty print a new line.
     * If pretty print is not enabled, this method does nothing
     *
     * @param bob   the string builder
     * @param depth the depth
     */
    private void prettyPrintNewLine(final StringBuilder bob, final int depth) {
        if (this.prettyPrintIndent == null) {
            return;
        }
        bob.append('\n');
        bob.append(this.prettyPrintIndent.repeat(depth));
    }

    /**
     * Write an array to a string builder
     *
     * @param bob      the string builder
     * @param depth    the depth
     * @param iterator the iterator of the array
     */
    public void writeArray(final StringBuilder bob, final int depth, final Iterator<Object> iterator) {
        bob.append('[');
        int i = 0;
        while (iterator.hasNext()) {
            final Object next = iterator.next();
            // if serialize nulls is not enabled, skip null values
            if (next == null && !this.serializeNulls) {
                continue;
            }
            if (i++ != 0) {
                bob.append(',');
            }
            this.prettyPrintNewLine(bob, depth + 1);
            this.write(bob, null, next, depth + 1);
        }

        this.prettyPrintNewLine(bob, depth);
        bob.append(']');
    }

    /**
     * Write a string to a string builder
     *
     * @param bob   the string builder
     * @param value the string
     */
    public void writeString(final StringBuilder bob, final String value) {
        bob.append('"');
        bob.append(JsonWriterMapper.escapeString(value));
        bob.append('"');
    }

    /**
     * Write an enum to a string builder
     *
     * @param bob   the string builder
     * @param value the enum
     */
    public void writeEnum(final StringBuilder bob, final Enum<?> value) {
        this.writeString(bob, value.name());
    }

    /**
     * Write an object to a string builder
     *
     * @param bob    the string builder
     * @param object the object
     * @param depth  the depth
     */
    public void writeObject(final StringBuilder bob, final Object object, final int depth) {
        bob.append('{');

        int i = 0;
        final Map<String, Field> fields = JsonMapperUtil.getDeclaredFieldsInClassAndSuperClasses(object.getClass());
        for (final Field field : fields.values()) {
            field.setAccessible(true);

            final Object value;
            try {
                value = field.get(object);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Failed to access field: " + field, e);
            }

            // if serialize nulls is not enabled, skip null values
            if (value == null && !this.serializeNulls) {
                continue;
            }

            if (i++ != 0) {
                bob.append(',');
            }
            this.prettyPrintNewLine(bob, depth + 1);

            // write name
            final String propertyName = field.isAnnotationPresent(JsonProperty.class)
                    ? field.getAnnotation(JsonProperty.class).value()
                    : field.getName();
            this.writeString(bob, propertyName);
            bob.append(": ");

            // write value
            this.write(bob, field, value, depth + 1);
        }

        this.prettyPrintNewLine(bob, depth);
        bob.append('}');
    }

    /**
     * Get the value writer mapper for a field
     *
     * @param field the field
     * @param clazz the class of the object
     * @return the value writer mapper
     */
    private ValueWriterMapper getValueWriterMapper(final Field field, final Class<?> clazz) {
        if (field != null && field.isAnnotationPresent(JsonMappper.class)) {
            final JsonMappper jsonMapperAnnotation = field.getAnnotation(JsonMappper.class);
            if (jsonMapperAnnotation.write() != ValueWriterMapper.class) {
                return JsonMapperUtil.getMapperFromValueMapperClass(
                        jsonMapperAnnotation.write(),
                        this.instantiatedValueWriterMapperMap
                );
            }
        }
        final List<FieldMapper<ValueWriterMapper>> mappers = this.valueWriterMapperMap.get(clazz);
        if (mappers != null) {
            for (final FieldMapper<ValueWriterMapper> mapper : mappers) {
                if (mapper.predicate() == null || mapper.predicate().test(field)) {
                    return mapper.mapper();
                }
            }
        }
        return null;
    }

    /**
     * Serialize an object to JSON
     *
     * @param bob    the string builder to write to
     * @param object the object to serialize
     * @param depth  the depth of the object
     */
    public void write(final StringBuilder bob, final Field field, final Object object, final int depth) {
        if (object == null) {
            bob.append(JsonPrimitive.NULL);
            return;
        }

        final Class<?> clazz = object.getClass();

        // arrays should be JsonArrays
        if (clazz.isArray()) {
            final Collection<Object> values = new ArrayList<>();
            final int length = Array.getLength(object);
            for (int i = 0; i < length; i++) {
                values.add(Array.get(object, i));
            }
            this.writeArray(bob, depth, values.iterator());
            return;
        }

        // list-likes should be JsonArrays
        if (Collection.class.isAssignableFrom(clazz)) {
            final Collection<Object> values = new ArrayList<>(((Collection<?>) object));
            this.writeArray(bob, depth, values.iterator());
            return;
        }

        final ValueWriterMapper valueWriterMapper = this.getValueWriterMapper(field, clazz);
        if (valueWriterMapper != null) {
            valueWriterMapper.writeValue(this, bob, field, object);
            return;
        }

        // enums should be identified by name
        if (clazz.isEnum()) {
            this.writeEnum(bob, (Enum<?>) object);
            return;
        }

        // check if primitive type
        if (String.class.equals(clazz) || JsonMapperUtil.WRAPPER_TYPES.contains(clazz)) {
            this.writeString(bob, JsonWriterMapper.escapeString(Objects.toString(object)));
            return;
        }

        this.writeObject(bob, object, depth);
    }

}
