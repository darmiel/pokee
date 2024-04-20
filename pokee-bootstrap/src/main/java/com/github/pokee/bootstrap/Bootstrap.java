package com.github.pokee.bootstrap;

import com.github.pokee.bootstrap.config.WebServerConfig;
import com.github.pokee.bootstrap.handlers.HelloWorldHandler;
import com.github.pokee.bootstrap.handlers.JsonErrorHandler;
import com.github.pokee.pson.Pson;
import com.github.pokee.pswf.connection.WebServer;
import com.github.pokee.pswf.connection.WebServerBuilder;
import com.github.pokee.pswf.exception.request.RequestException;
import com.github.pokee.pswf.request.Method;
import com.github.pokee.pswf.response.ResponseBuilder;

import java.io.IOException;

public class Bootstrap {

    public static void main(String[] args) throws IOException {
        final Pson pson = Pson.createWithDefaults().build();
        final WebServerConfig config = pson.unmarshalObject(WebServerConfig.class);

        final WebServer server = new WebServerBuilder(8080)
                .parameterized()
                .errorHandler(new JsonErrorHandler())
                // single route middleware example
                .register(Method.GET, "/hello/:name", context -> {
                    if (context.queryInt("age", 0) < 18) {
                        throw new RequestException("You must be 18 or older to access this resource");
                    }
                    System.out.println("[Middleware] Handling request for /hello/:name");
                    // continue request in next handler
                })
                .get("/", ctx -> new ResponseBuilder()
                        .text("Hello World from Pokee API!"))
                .clazz(new HelloWorldHandler())
                .build();

        System.out.println("Starting server on port " + config.getPort());
        server.start();
    }

}
