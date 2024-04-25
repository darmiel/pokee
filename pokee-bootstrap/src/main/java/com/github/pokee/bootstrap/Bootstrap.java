package com.github.pokee.bootstrap;

import com.github.pokee.bootstrap.config.WebServerConfig;
import com.github.pokee.bootstrap.handlers.HelloWorldHandler;
import com.github.pokee.bootstrap.handlers.JsonErrorHandler;
import com.github.pokee.bootstrap.handlers.PokemonHandler;
import com.github.pokee.common.LocalizedString;
import com.github.pokee.common.Pokemon;
import com.github.pokee.pson.Pson;
import com.github.pokee.pson.value.JsonElement;
import com.github.pokee.pson.value.JsonObject;
import com.github.pokee.pswf.connection.WebServer;
import com.github.pokee.pswf.connection.WebServerBuilder;
import com.github.pokee.pswf.exception.request.RequestException;
import com.github.pokee.pswf.request.Method;
import com.github.pokee.pswf.response.ResponseBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Bootstrap {

    public static void main(String[] args) throws IOException {
        final Pson pson = Pson.createWithDefaults()
                .registerValueReaderMapper(LocalizedString.class, (element, field) -> {
                    final JsonObject object = element.asObject();
                    final LocalizedString localizedString = new LocalizedString();
                    for (final Map.Entry<String, JsonElement> entry : object.entries()) {
                        localizedString.put(entry.getKey(), entry.getValue().asPrimitive().asString());
                    }
                    return localizedString;
                })
                .build();

        // load server config
        final WebServerConfig config = pson.unmarshalObject(WebServerConfig.class);

        // load pokemons data
        final List<Pokemon> pokemons = pson.unmarshalList(Pokemon.class);

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
                .clazz(new PokemonHandler(pokemons))
                .build();

        System.out.println("Starting server on port " + config.getPort());
        server.start();
    }

}
