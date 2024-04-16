package com.github.pokee.stick;

import com.github.pokee.stick.connection.BasicRouterClientHandler;
import com.github.pokee.stick.connection.WebServer;
import com.github.pokee.stick.response.ResponseBuilder;
import com.github.pokee.stick.router.Router;

import java.io.IOException;

public class Bootstrap {

    public static void main(String[] args) throws IOException {
        final Router router = new Router();
        router.registerRoute(Method.GET, "/", (req) -> new ResponseBuilder().text("Hello World! /").build());
        router.registerRoute(Method.GET, "/:name", (req) -> new ResponseBuilder().text("Hello, someone").build());

        final WebServer server = new WebServer(8080, new BasicRouterClientHandler(router));

        server.start();
    }

}
