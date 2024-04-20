package com.github.pokee.stick.router;

import com.github.pokee.stick.response.ResponseLike;

import java.util.function.Function;

/**
 * Represents a handler for processing HTTP requests within a specific application context.
 * Handlers are designed to take a context object, which provides request details and allows
 * setting the response. This interface uses the implementation of request handling
 * logic that can be encapsulated and reused across different parts of the application.
 */
public interface Handler {

    /**
     * Wraps a function that handles an HTTP request and returns a {@link ResponseLike} object into a Handler.
     * This allows using simple lambda expressions or method references as Handlers, simplifying the creation
     * of response-generating logic.
     *
     * @param handler A function that takes a Context and returns a {@link ResponseLike}.
     * @return A Handler that sets the response in the context based on the output of the function.
     */
    static Handler wrap(final Function<Context, ResponseLike> handler) {
        return context -> context.response = handler.apply(context).extractResponse();
    }

    /**
     * Processes an HTTP request and sets the response based on the request context.
     *
     * @param context The context of the HTTP request, containing both the request details
     *                and the response to be manipulated.
     */
    void handle(final Context context);

}
