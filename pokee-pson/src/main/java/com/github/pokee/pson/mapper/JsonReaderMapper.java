package com.github.pokee.pson.mapper;

import com.github.pokee.pson.exception.TokenTypeExpectedException;
import com.github.pokee.pson.mapper.annotations.JsonIgnore;
import com.github.pokee.pson.mapper.annotations.JsonMappper;
import com.github.pokee.pson.mapper.annotations.JsonOptional;
import com.github.pokee.pson.mapper.annotations.JsonProperty;
import com.github.pokee.pson.value.JsonArray;
import com.github.pokee.pson.value.JsonElement;
import com.github.pokee.pson.value.JsonObject;
import com.github.pokee.pson.value.JsonPrimitive;
import sun.misc.Unsafe;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public class JsonReaderMapper {

    private final Map<Class<?>, List<FieldMapper<ValueReaderMapper>>> valueReaderMapperMap;

    private final Map<Class<? extends ValueReaderMapper>, ValueReaderMapper> instantiatedValueReaderMapperMap;

    private Unsafe unsafe;

    public JsonReaderMapper(
            final Map<Class<?>, List<FieldMapper<ValueReaderMapper>>> valueReaderMapperMap
    ) {
        this.valueReaderMapperMap = valueReaderMapperMap;
        this.instantiatedValueReaderMapperMap = new HashMap<>();
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
            for (final Map.Entry<Character, String> entry : JsonMapperUtil.ESCAPE_MAP.entrySet()) {
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
     * Get the unsafe instance
     *
     * @return the unsafe instance
     * @throws NoSuchFieldException   if the field is not found
     * @throws IllegalAccessException if the field is not accessible
     */
    private Unsafe getUnsafeIllegally() throws NoSuchFieldException, IllegalAccessException {
        if (this.unsafe != null) {
            return this.unsafe;
        }
        final Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        return this.unsafe = (Unsafe) field.get(null);
    }

    /**
     * Get the value reader mapper for a field
     *
     * @param field the field
     * @param clazz the class of the object
     * @return the value reader mapper
     */
    private ValueReaderMapper getValueReaderMapper(final Field field, final Class<?> clazz) {
        if (field != null && field.isAnnotationPresent(JsonMappper.class)) {
            final JsonMappper jsonMapperAnnotation = field.getAnnotation(JsonMappper.class);
            if (jsonMapperAnnotation.read() != ValueReaderMapper.class) {
                return JsonMapperUtil.getMapperFromValueMapperClass(
                        jsonMapperAnnotation.read(),
                        this.instantiatedValueReaderMapperMap
                );
            }
        }
        final List<FieldMapper<ValueReaderMapper>> mappers = this.valueReaderMapperMap.get(clazz);
        if (mappers != null) {
            for (final FieldMapper<ValueReaderMapper> mapper : mappers) {
                if (mapper.predicate() == null || mapper.predicate().test(field)) {
                    return mapper.mapper();
                }
            }
        }
        return null;
    }

    /**
     * Get the value for a field
     *
     * @param element the JSON element
     * @param key     the key of the field
     * @param field   the field
     * @return the value
     */
    @SuppressWarnings("unchecked")
    public Object getValueForField(
            final JsonElement element,
            final String key,
            final Field field
    ) throws TokenTypeExpectedException, ReflectiveOperationException {
        final Class<?> fieldType = field.getType();

        if (element.isPrimitive() && element.asPrimitive().isNull()) {
            return null;
        }

        if (fieldType.isArray()) {
            if (!element.isArray()) {
                throw new IllegalStateException("Expected array for array-field: " + key);
            }
            final Class<?> componentType = fieldType.getComponentType();
            return this.readArray(element.asArray(), componentType);
        }

        if (Collection.class.isAssignableFrom(fieldType)) {
            if (!element.isArray()) {
                throw new IllegalStateException("Expected array for list-field: " + key);
            }
            final ParameterizedType genericType = (ParameterizedType) field.getGenericType();
            return this.readArrayAsList(element.asArray(), (Class<?>) genericType.getActualTypeArguments()[0]);
        }

        final ValueReaderMapper mapper = this.getValueReaderMapper(field, fieldType);
        if (mapper != null) {
            return mapper.mapValue(element, field);
        }

        if (fieldType.isEnum()) {
            if (!element.isPrimitive()) {
                throw new IllegalStateException("Expected e/string for enum-field: " + key);
            }
            final JsonPrimitive primitive = element.asPrimitive();
            if (!primitive.isString()) {
                throw new IllegalStateException("Expected p/string for enum-field: " + key);
            }
            //noinspection rawtypes
            return Enum.valueOf((Class<Enum>) fieldType, primitive.asString());
        }

        if (element.isObject()) {
            return this.readObject(element.asObject(), fieldType);
        }

        throw new UnsupportedOperationException("Cannot parse object of type: " + fieldType.getName() + " from JSON");
    }

    /**
     * Read a JSON string and return an object with the values from the JSON.
     * It is expected that the object has a default constructor.
     * <p>
     * Note that if the json contains a field that is not present in the object, an exception will be thrown unless the
     * field is annotated by {@link JsonOptional}
     * (or {@link JsonIgnore}).
     * </p>
     *
     * @param jsonObject the JSON object
     * @param clazz      the class of the object to read
     * @param <T>        the type of the object to read
     * @return the object
     * @throws TokenTypeExpectedException if the JSON is invalid
     * @throws IllegalStateException      if the JSON is not an object
     */
    public <T> T readObject(
            final JsonObject jsonObject,
            final Class<T> clazz
    ) throws TokenTypeExpectedException, ReflectiveOperationException {
        // this is my first time using Unsafe, how exciting!
        final Unsafe unsafe = this.getUnsafeIllegally();

        // allocate an instance of clazz using Unsafe
        // we don't want to go through the constructor since we want to set the fields manually
        //noinspection unchecked (this should be safe since we specify the type of the object)
        final T object = (T) unsafe.allocateInstance(clazz);

        // collect all fields from the object and its superclasses
        final Map<String, Field> fields = JsonMapperUtil.getDeclaredFieldsInClassAndSuperClasses(clazz);

        // now it's time to populate the fields
        for (final Field field : fields.values()) {
            final String key = field.isAnnotationPresent(JsonProperty.class) ?
                    field.getAnnotation(JsonProperty.class).value() :
                    field.getName();

            // if the JSON object does not contain the field, throw an exception if the field is not optional
            if (!jsonObject.has(key)) {
                if (!field.isAnnotationPresent(JsonOptional.class)) {
                    throw new IllegalStateException("Missing required field: " + key);
                }
                continue;
            }

            final JsonElement element = jsonObject.get(key);
            final Object value = this.getValueForField(element, key, field);
            if (value == null) {
                continue;
            }

            final long offset = unsafe.objectFieldOffset(field);

            if (Integer.class.equals(value.getClass())) {
                unsafe.putInt(object, offset, (int) value);
            } else if (Long.class.equals(value.getClass())) {
                unsafe.putLong(object, offset, (long) value);
            } else if (Short.class.equals(value.getClass())) {
                unsafe.putShort(object, offset, (short) value);
            } else if (Double.class.equals(value.getClass())) {
                unsafe.putDouble(object, offset, (double) value);
            } else if (Float.class.equals(value.getClass())) {
                unsafe.putFloat(object, offset, (float) value);
            } else if (Byte.class.equals(value.getClass())) {
                unsafe.putByte(object, offset, (byte) value);
            } else if (Character.class.equals(value.getClass())) {
                unsafe.putChar(object, offset, (char) value);
            } else if (Boolean.class.equals(value.getClass())) {
                unsafe.putBoolean(object, offset, (boolean) value);
            } else {
                unsafe.putObject(object, offset, value);
            }
        }

        return object;
    }

    /**
     * Read a JSON element
     *
     * @param element the JSON element
     * @param clazz   the class of the object to read
     * @return the object
     */
    public Object readJsonElement(final JsonElement element, final Class<?> clazz) throws TokenTypeExpectedException, ReflectiveOperationException {
        if (element.isArray()) {
            return this.readArray(element.asArray(), clazz);
        }
        if (element.isObject()) {
            return this.readObject(element.asObject(), clazz);
        }
        if (element.isPrimitive()) {
            final JsonPrimitive primitive = element.asPrimitive();
            if (primitive.isNull()) {
                return null;
            }
            if (primitive.isBoolean()) {
                return primitive.asBoolean();
            }
            if (primitive.isString()) {
                return primitive.asString();
            }
            if (primitive.isInteger()) {
                return primitive.asInteger();
            }
            if (primitive.isDouble()) {
                return primitive.asDouble();
            }
        }
        throw new UnsupportedOperationException("Cannot parse object of type: " + clazz.getName() + " from JSON");
    }

    /**
     * Read a JSON array
     *
     * @param jsonArray the JSON array
     * @param clazz     the class of the object to read
     * @param <T>       the type of the object to read
     * @return the array
     */
    public <T> T[] readArray(final JsonArray jsonArray, final Class<T> clazz) throws TokenTypeExpectedException, ReflectiveOperationException {
        // create and populate array using reflection from the json element/s
        final Object array = Array.newInstance(clazz, jsonArray.size());
        for (int i = 0; i < jsonArray.size(); i++) {
            Array.set(array, i, this.readJsonElement(jsonArray.get(i), clazz));
        }
        // we can be sure that the array is of type T[] since we created it using reflection
        //noinspection unchecked
        return (T[]) array;
    }

    /**
     * Read a JSON array as a list
     *
     * @param jsonArray the JSON array
     * @param clazz     the class of the object to read
     * @param <T>       the type of the object to read
     * @return the list
     */
    public <T> List<T> readArrayAsList(final JsonArray jsonArray, final Class<T> clazz) throws TokenTypeExpectedException, ReflectiveOperationException {
        return Arrays.asList(this.readArray(jsonArray, clazz));
    }

}
