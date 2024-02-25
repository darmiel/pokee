package com.github.pokee.json;

import com.github.pokee.json.exception.MissingRequiredFieldException;
import com.github.pokee.json.exception.TokenTypeExpectedException;
import com.github.pokee.json.mapper.ObjectMapper;
import com.github.pokee.json.mapper.annotations.Ignored;
import com.github.pokee.json.mapper.annotations.Optional;
import com.github.pokee.json.mapper.annotations.Property;

import java.lang.reflect.InvocationTargetException;

public class Bootstrap {

    static class Person {

        Person person;

        // by annotation
        @Property("name")
        private String myName;

        // by field name
        private int age;

        // ignored field
        @Ignored
        private boolean isStudent;

        @Optional
        public String hello;

        public Person() {}

        @Override
        public String toString() {
            return "Person{" +
                    "myName='" + myName + '\'' +
                    ", age=" + age +
                    ", isStudent=" + isStudent +
                    '}';
        }
    }

    public static void main(String[] args) throws TokenTypeExpectedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, MissingRequiredFieldException {
        String json = "{\"name\":\"John\",\"age\":30,\"person\":{\"name\":\"Tobias\",\"age\":9, \"person\": null}}";
        final Person person = ObjectMapper.parse(json, Person.class);
        System.out.println(person);
        System.out.println(JsonWriter.objectToJson(person, "  ", 0));

//        final JsonParser parser = new JsonParser(json);
//        final JsonElement element = parser.parse();
//
//        if (element.isObject()) {
//            final Person mapped = ObjectMapper.mapJsonObject(element.asObject(), Person.class);
//            System.out.println("Parsed person: " + mapped);
//            System.out.println("Parsed object: " + element.asObject());
//        }
//
//        if (element.isArray()) {
//            System.out.println("Parsed array: " + element.asArray());
//        }
//
//        if (element.isPrimitive()) {
//            System.out.println("Parsed primitive: " + element.asPrimitive());
//asPrimitive        }
    }

}
