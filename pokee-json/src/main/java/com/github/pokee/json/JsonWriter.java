package com.github.pokee.json;

import com.github.pokee.json.mapper.annotations.Property;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class JsonWriter {

    private static final Set<Class<?>> WRAPPER_TYPES = new HashSet<>() {{
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

    public static String objectToJson(
            final Object object,
            final String indent,
            final int indentLevel
    ) throws IllegalAccessException {
        if (object == null) {
            return "null";
        }

        if (String.class.equals(object.getClass()) || WRAPPER_TYPES.contains(object.getClass())) {
            return object.toString();
        }

        final StringBuilder bob = new StringBuilder();
        bob.append("{\n");
        for (final Field field : object.getClass().getDeclaredFields()) {
            final String key = field.isAnnotationPresent(Property.class)
                    ? field.getAnnotation(Property.class).value()
                    : field.getName();

            System.out.println("saving field " + field.getName() + " for " + object.getClass().getName() + " as " + key);

            field.setAccessible(true);
            final Object value = field.get(object);

            final String jsonValue = JsonWriter.objectToJson(value, indent, indentLevel + 1);
            bob.append(indent.repeat(indentLevel + 1))
                    .append("\"")
                    .append(key)
                    .append("\": ")
                    .append(jsonValue)
                    .append(",\n");
        }
        bob.append(indent.repeat(indentLevel)).append("}");
        return bob.toString();
    }

}
