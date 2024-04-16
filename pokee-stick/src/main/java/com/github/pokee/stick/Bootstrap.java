package com.github.pokee.stick;

import com.github.pokee.stick.connection.WebServer;
import com.github.pokee.stick.response.ResponseBuilder;

import java.io.IOException;
import java.util.UUID;

public class Bootstrap {

    public record ResponseObject(UUID id, String name) {
    }

    public static void main(String[] args) throws IOException {
        final WebServer server = new WebServer(8080);

        server.GET("/", (req) -> new ResponseBuilder()
                .status(200)
                .text("Hello World!")
                .build());

        server.GET("/json", (req) -> new ResponseBuilder()
                .status(200)
                .json(new ResponseObject(UUID.randomUUID(), "Pokee"))
                .build());

        server.start();
    }

}
