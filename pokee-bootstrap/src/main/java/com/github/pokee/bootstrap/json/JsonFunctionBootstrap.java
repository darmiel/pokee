package com.github.pokee.bootstrap.json;

import com.github.pokee.pson.Pson;
import com.github.pokee.pson.value.JsonElement;

public class JsonFunctionBootstrap {

    public static void main(String[] args) {
        final String json = """
                {
                    // this is a comment!
                    "secret": @env("SECRET"), // this is also a comment
                    // and this
                    "person": @file("person.json", {"optional": true, "json": true}), // hehe
                    // hi!
                    "test" : " o k "
                }""";

        final Pson pson = Pson.createWithDefaults()
                .expandFunctions()
                .build();

        final JsonElement element = pson.unmarshal(json);
        System.out.println(element);
    }

}
