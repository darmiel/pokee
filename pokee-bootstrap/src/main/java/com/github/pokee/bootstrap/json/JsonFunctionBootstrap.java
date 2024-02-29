package com.github.pokee.bootstrap.json;

import com.github.pokee.json.exception.TokenTypeExpectedException;
import com.github.pokee.json.parser.JsonFunctionRunner;
import com.github.pokee.json.parser.JsonParser;
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

        json = String.join("\n", Files.readAllLines(Paths.get("config/config.json")));

        final JsonParser parser = new JsonParser(json, true, JsonFunctionRunner.DEFAULT);
        final JsonElement element = parser.parse();
        System.out.println(element);
    }

}
