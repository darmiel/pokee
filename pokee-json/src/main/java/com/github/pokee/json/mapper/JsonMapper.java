package com.github.pokee.json.mapper;

import com.github.pokee.json.mapper.annotations.JsonIgnore;
import com.github.pokee.json.mapper.annotations.JsonMappper;
import com.github.pokee.json.mapper.annotations.JsonProperty;
import com.github.pokee.json.mapper.annotations.JsonScope;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class JsonMapper {

    public static final String NULL = "null";
    public static final String TRUE = "true";
    public static final String FALSE = "false";

    /**
     * A set of all wrapper types.
     * These are the types that can be directly converted to a string.
     */
    public static final Set<Class<?>> WRAPPER_TYPES = new HashSet<>() {{
        add(Boolean.class);
        add(Character.class);
        add(Byte.class);
        add(Short.class);
        add(Integer.class);
        add(Long.class);
        add(Float.class);
        add(Double.class);
        add(Void.class);
    }};

    /**
     * A map of the characters that should be (un)escaped
     */
    public static final Map<Character, String> ESCAPE_MAP = new HashMap<>() {{
        // double quote
        put((char) 0x22, "\\\"");
        // backslash
        put((char) 0x5C, "\\\\");
        // NULL character
        put((char) 0x00, "\\u0000");
        // START OF HEADING character
        put((char) 0x01, "\\u0001");
        // START OF TEXT character
        put((char) 0x02, "\\u0002");
        // END OF TEXT character
        put((char) 0x03, "\\u0003");
        // END OF TRANSMISSION character
        put((char) 0x04, "\\u0004");
        // ENQUIRY character
        put((char) 0x05, "\\u0005");
        // ACKNOWLEDGE character
        put((char) 0x06, "\\u0006");
        // BELL character
        put((char) 0x07, "\\u0007");
        // BACKSPACE character
        put((char) 0x08, "\\b");
        // CHARACTER TABULATION character
        put((char) 0x09, "\\t");
        // LINE FEED character
        put((char) 0x0A, "\\n");
        // LINE TABULATION character
        put((char) 0x0B, "\\u000B");
        // FORM FEED character
        put((char) 0x0C, "\\f");
        // CARRIAGE RETURN character
        put((char) 0x0D, "\\r");
        // SHIFT OUT character
        put((char) 0x0E, "\\u000E");
        // SHIFT IN character
        put((char) 0x0F, "\\u000F");
        // DELETE character
        put((char) 0x7F, "\\u007F");
    }};


    private final String prettyPrintIndent;
    private final boolean serializeNulls;

    private final Map<Class<?>, List<FieldMapper<ValueWriterMapper>>> valueWriterMapperMap;
    private final Map<Class<?>, ValueReaderMapper> valueReaderMapperMap;

    private final Map<Class<? extends ValueWriterMapper>, ValueWriterMapper> instantiatedValueWriterMapperMap;
    private final Map<Class<? extends ValueReaderMapper>, ValueReaderMapper> instantiatedValueReaderMapperMap;

    public JsonMapper(
            final boolean serializeNulls,
            final String prettyPrintIndent,
            final Map<Class<?>, List<FieldMapper<ValueWriterMapper>>> valueWriterMapperMap,
            final Map<Class<?>, ValueReaderMapper> valueReaderMapperMap
    ) {
        this.serializeNulls = serializeNulls;
        this.prettyPrintIndent = prettyPrintIndent;

        this.valueWriterMapperMap = valueWriterMapperMap;
        this.valueReaderMapperMap = valueReaderMapperMap;

        this.instantiatedValueWriterMapperMap = new HashMap<>();
        this.instantiatedValueReaderMapperMap = new HashMap<>();
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
            final String escape = ESCAPE_MAP.get(character);
            if (escape != null) {
                bob.append(escape);
            } else {
                bob.append(character);
            }
        }
        return bob.toString();
    }

    /**
     * Unescape a string
     *
     * @param escapedInput the escaped input string
     * @return the unescaped string
     * @throws IllegalArgumentException if the input string contains an invalid escape sequence
     * @throws NumberFormatException    if the input string contains an invalid unicode escape sequence
     */
    public static String unescapeString(final String escapedInput) {
        final StringBuilder bob = new StringBuilder();

        loop:
        for (int i = 0; i < escapedInput.length(); i++) {
            final char character = escapedInput.charAt(i);
            if (character != '\\') {
                bob.append(character);
                continue;
            }

            // if no more characters are available, throw an exception since the escape sequence is incomplete
            if (i == escapedInput.length() - 1) {
                throw new IllegalArgumentException("Invalid escape sequence");
            }

            final char next = escapedInput.charAt(i + 1);

            // unescape backslash
            if (next == '\\') {
                bob.append('\\');
                i++;
                continue;
            }

            // unescape unicode characters
            if (next == 'u') {
                if (i + 5 >= escapedInput.length()) {
                    throw new IllegalArgumentException("Invalid escape sequence \\u[...]");
                }
                final String hex = escapedInput.substring(i + 2, i + 6);
                bob.append((char) Integer.parseInt(hex, 16));
                i += 5;
                continue;
            }

            // find escape sequence in ESCPAE_MAP
            for (final Map.Entry<Character, String> entry : JsonMapper.ESCAPE_MAP.entrySet()) {
                if (entry.getValue().equals("\\" + next)) {
                    bob.append(entry.getKey());
                    i++;
                    continue loop;
                }
            }

            throw new IllegalArgumentException("Invalid escape sequence \\" + next);
        }
        return bob.toString();
    }

    /**
     * Get the value reader mapper for a field
     *
     * @param mapperClass           the value mapper class
     * @param instantiatedMapperMap the instantiated value mapper map
     * @param <T>                   the type of the value mapper
     * @return the value reader mapper
     */
    public <T> T getMapperFromValueMapperClass(
            final Class<? extends T> mapperClass,
            final Map<Class<? extends T>, T> instantiatedMapperMap
    ) {
        // if the value mapper is already instantiated, return it
        if (instantiatedMapperMap.containsKey(mapperClass)) {
            return instantiatedMapperMap.get(mapperClass);
        }
        // if the value mapper is not instantiated, instantiate it and store it in the map
        final T mapper;
        try {
            mapper = mapperClass.getDeclaredConstructor().newInstance();
        } catch (final ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to instantiate value mapper: " + mapperClass, e);
        }
        instantiatedMapperMap.put(mapperClass, mapper);
        return mapper;
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

    public void writeString(final StringBuilder bob, final String value) {
        bob.append('"');
        bob.append(JsonMapper.escapeString(value));
        bob.append('"');
    }

    public void writeEnum(final StringBuilder bob, final Enum<?> value) {
        this.writeString(bob, value.name());
    }

    public void writeObject(final StringBuilder bob, final Object object, final int depth) {
        bob.append('{');

        final Class<?> clazz = object.getClass();
        final JsonScope scope = clazz.getAnnotation(JsonScope.class);

        boolean transientFields = scope != null ? scope.transientFields() : JsonScope.DEFAULT_TRANSIENT_FIELDS;
        boolean publicFields = scope != null ? scope.publicFields() : JsonScope.DEFAULT_PUBLIC_FIELDS;
        boolean privateFields = scope != null ? scope.privateFields() : JsonScope.DEFAULT_PRIVATE_FIELDS;

        int i = 0;
        for (final Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(JsonIgnore.class)
                    || Modifier.isTransient(field.getModifiers()) && !transientFields
                    || Modifier.isPublic(field.getModifiers()) && !publicFields
                    || Modifier.isPrivate(field.getModifiers()) && !privateFields) {
                continue;
            }

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

    private ValueWriterMapper getValueWriterMapper(final Field field, final Class<?> clazz) {
        if (field != null && field.isAnnotationPresent(JsonMappper.class)) {
            final JsonMappper jsonMapperAnnotation = field.getAnnotation(JsonMappper.class);
            if (jsonMapperAnnotation.write() != ValueWriterMapper.class) {
                return this.getMapperFromValueMapperClass(
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
            bob.append(JsonMapper.NULL);
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
        if (String.class.equals(clazz) || WRAPPER_TYPES.contains(clazz)) {
            this.writeString(bob, JsonMapper.escapeString(Objects.toString(object)));
            return;
        }

        this.writeObject(bob, object, depth);
    }

}
