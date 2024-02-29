package com.github.pokee.bootstrap.json;

import com.github.pokee.json.Pson;
import com.github.pokee.json.exception.TokenTypeExpectedException;
import com.github.pokee.json.value.JsonElement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonFunctionBootstrap {

    public static void main(String[] args) throws TokenTypeExpectedException, IOException {
        String json = """
                {
                    "secret": @env("SECRET"),
                    "person": @file("person.json", {"optional": true, "parseJson": true})
                }""";

        json = String.join("\n", Files.readAllLines(Paths.get("config/config.pson")));

        final Pson pson = Pson.createWithDefaults()
                .expandFunctions()
                .build();

        final JsonElement element = pson.unmarshal(json);
        System.out.println(element);
    }

}
