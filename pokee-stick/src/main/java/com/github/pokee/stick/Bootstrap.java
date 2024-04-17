package com.github.pokee.stick;

import com.github.pokee.stick.connection.BasicRouterClientHandler;
import com.github.pokee.stick.connection.WebServer;
import com.github.pokee.stick.response.ResponseBuilder;
import com.github.pokee.stick.router.Handler;
import com.github.pokee.stick.router.ParameterizableRouter;

import java.io.IOException;

public class Bootstrap {

    public static void main(String[] args) throws IOException {
        final ParameterizableRouter router = new ParameterizableRouter();

        router.registerRoute(Method.GET, "/", Handler.wrapBuilder((ctx) -> new ResponseBuilder()
                .text(ctx.hasQuery("name") ? "Hello from query, " + ctx.query("name") : "Hello World!")
        ));

        router.registerRoute(Method.GET, "/:name", Handler.wrap(ctx -> new ResponseBuilder()
                .text("Hello from params, " + ctx.param("name"))
                .build()
        ));

        final WebServer server = new WebServer(8080, new BasicRouterClientHandler(router));
        server.start();
    }

}
