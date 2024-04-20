package com.github.pokee.bootstrap;

import com.github.pokee.bootstrap.config.WebServerConfig;
import com.github.pokee.bootstrap.handlers.JsonErrorHandler;
import com.github.pokee.pson.Pson;
import com.github.pokee.stick.connection.WebServer;
import com.github.pokee.stick.connection.WebServerBuilder;
import com.github.pokee.stick.response.ResponseBuilder;

import java.io.IOException;

public class Bootstrap {

    public static void main(String[] args) throws IOException {
        final Pson pson = Pson.createWithDefaults().build();
        final WebServerConfig config = pson.unmarshalObject(WebServerConfig.class);

        final WebServer server = new WebServerBuilder(8080)
                .parameterized()
                .errorHandler(new JsonErrorHandler())
                .get("/", ctx -> new ResponseBuilder()
                        .text("Hello World!"))
                .get("/hello", ctx -> new ResponseBuilder()
                        .text("Hello!"))
                .get("/hello/:name", ctx -> new ResponseBuilder()
                        .text("Hello, " + ctx.param("name") + "!"))
                .build();

        System.out.println("Starting server on port " + config.getPort());
        server.start();
    }

}
