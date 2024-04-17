package com.github.pokee.stick.router;

import com.github.pokee.stick.response.Response;
import com.github.pokee.stick.response.ResponseBuilder;

import java.util.function.Function;

/**
 * Represents a handler for processing HTTP requests within a specific application context.
 * Handlers are designed to take a context object, which provides request details and allows
 * setting the response.
 */
public interface Handler {

    /**
     * Wraps a function that handles an HTTP request and returns a Response object into a Handler.
     * This allows using simple lambda expressions or method references as Handlers.
     *
     * @param handler A function that takes a Context and returns a Response.
     * @return A Handler that sets the response in the context.
     */
    static Handler wrap(final Function<Context, Response> handler) {
        return context -> context.response = handler.apply(context);
    }

    /**
     * Wraps a function that constructs a {@link ResponseBuilder} for building a response.
     * This method allows the handler to build a response using a fluent API before setting it
     * in the context.
     *
     * @param handlerBuilder A function that takes a Context and returns a ResponseBuilder.
     * @return A Handler that constructs and sets the response in the context upon execution.
     */
    static Handler wrapBuilder(final Function<Context, ResponseBuilder> handlerBuilder) {
        return context -> context.response = handlerBuilder.apply(context).build();
    }

    /**
     * Processes an HTTP request and sets the response based on the request context.
     *
     * @param context The context of the HTTP request, containing both the request details
     *                and the response to be manipulated.
     */
    void handle(final Context context);

}

