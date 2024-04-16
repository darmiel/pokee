package com.github.pokee.bootstrap.json;

import com.github.pokee.pson.Pson;
import com.github.pokee.pson.mapper.annotations.JsonOptional;

import java.util.Arrays;
import java.util.List;

public class JsonUnmarshalBootstrap {

    @SuppressWarnings("unused")
    record Test(String name, int age) {
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    class Test2 {
        private final String name;
        private final int age;

        @JsonOptional
        private final List<Test2> inner;

        public Test2(String name, int age, Test2... inner) {
            this.name = name;
            this.age = age;
            this.inner = Arrays.asList(inner);
        }

        @Override
        public String toString() {
            return "Test2{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", inner=" + inner +
                    '}';
        }
    }

    public static void main(String[] args) {
        final Pson pson = Pson.createWithDefaults().build();

        final Test2[] tests = pson.unmarshalArray("""
                [{
                    "name": "Daniel",
                    "age": 21,
                    "inner": [{
                        "name": "Jan",
                        "age": 21
                    },{
                        "name": "Test",
                        "age": 99
                    }]
                },{
                    "name": "Test",
                    "age": 99
                }]
                """, Test2.class);
        System.out.println(Arrays.toString(tests));
    }

}
