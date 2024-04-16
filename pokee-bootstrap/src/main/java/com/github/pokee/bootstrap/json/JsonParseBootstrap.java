package com.github.pokee.bootstrap.json;

import com.github.pokee.pson.Pson;
import com.github.pokee.pson.exception.MissingRequiredFieldException;
import com.github.pokee.pson.exception.TokenTypeExpectedException;
import com.github.pokee.pson.mapper.annotations.JsonIgnore;
import com.github.pokee.pson.mapper.annotations.JsonOptional;
import com.github.pokee.pson.mapper.annotations.JsonProperty;
import com.github.pokee.pson.parser.JsonParser;

import java.lang.reflect.InvocationTargetException;

public class JsonParseBootstrap {

    static class Person {

        Person person;

        // by annotation
        @JsonProperty("name")
        private String myName;

        // by field name
        private int age;

        // ignored field
        @JsonIgnore
        private boolean isStudent;

        @JsonOptional
        public String hello;

        public Person() {
        }

        @Override
        public String toString() {
            return "Person{" +
                    "myName='" + myName + '\'' +
                    ", age=" + age +
                    ", isStudent=" + isStudent +
                    '}';
        }
    }

    public static void main(
            final String[] args
    ) throws TokenTypeExpectedException,
            InvocationTargetException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException,
            MissingRequiredFieldException {
        String json = "{\"name\":\"John\",\"age\":30,\"person\":{\"name\":\"Tobias\",\"age\":9, \"person\": null}}";
        new JsonParser(json).parse();

        final Pson pson = Pson.create()
                .prettyPrint()
                .serializeNulls()
                .build();
    }

}
