package com.github.pokee.json.mapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for JSON mappers
 */
public class JsonMapperUtil {

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

    /**
     * Get the value reader mapper for a field
     *
     * @param mapperClass           the value mapper class
     * @param instantiatedMapperMap the instantiated value mapper map
     * @param <T>                   the type of the value mapper
     * @return the value reader mapper
     */
    public static <T> T getMapperFromValueMapperClass(
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

}
