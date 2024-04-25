package com.github.pokee.common;


import com.github.pokee.common.fielder.Fielder;

public class Pokemon implements Fielder {

    private final int id;
    private final String name;
    private final int age;

    public Pokemon(final int id, final String name, final int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public Object getField(final String name) {
        return switch (name.toLowerCase()) {
            case "id" -> this.id;
            case "name" -> this.name;
            case "age" -> this.age;
            default -> throw new IllegalArgumentException("Field " + name + " not found");
        };
    }

    @Override
    public String[] getFields() {
        return new String[]{"id", "name", "age"};
    }

}
