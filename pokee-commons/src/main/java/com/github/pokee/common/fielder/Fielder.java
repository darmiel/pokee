package com.github.pokee.common.fielder;

public interface Fielder {

    String[] getFields();

    default boolean hasField(final String name) {
        for (final String field : this.getFields()) {
            if (field.equals(name)) {
                return true;
            }
        }
        return false;
    }

    Object getField(final String name);

}
