package com.github.pokee.pswf.util;

public class Enums {

    /**
     * Returns the enum constant of the specified enum type with the specified name.
     * The name must match exactly an identifier used to declare an enum constant in this type.
     *
     * @param enumClass the {@code Class} object of the enum type from which to return a constant
     * @param name      the name of the constant to return
     * @param <T>       the type of the enum
     * @return the enum constant of the specified enum type with the specified name
     */
    public static <T extends Enum<T>> T fromString(final Class<T> enumClass, final String name) {
        for (final T constant : enumClass.getEnumConstants()) {
            if (constant.name().equalsIgnoreCase(name)) {
                return constant;
            }
        }
        return null;
    }

}
