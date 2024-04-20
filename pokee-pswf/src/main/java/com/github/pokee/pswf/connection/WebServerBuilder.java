package com.github.pokee.pswf.connection;

import com.github.pokee.pswf.connection.handler.BasicRouterClientHandler;
import com.github.pokee.pswf.connection.handler.ClientHandler;
import com.github.pokee.pswf.request.Method;
import com.github.pokee.pswf.response.ResponseBuilder;
import com.github.pokee.pswf.response.ResponseLike;
import com.github.pokee.pswf.response.StatusCode;
import com.github.pokee.pswf.router.*;

import java.util.function.Function;

/**
 * A builder class for constructing a {@link WebServer}. This class provides a fluent interface
 * for configuring a WebServer, including its routing and error handling mechanisms.
 */
public class WebServerBuilder {

    private final int port;

    /**
     * Creates a new WebServerBuilder for configuring a WebServer on the specified port.
     *
     * @param port The port on which the server will listen.
     */
    public WebServerBuilder(int port) {
        this.port = port;
    }

    /**
     * Specifies the router to use with the web server.
     *
     * @param router The router for handling incoming requests.
     * @return A new instance of WebServerBuilderWithRouter for further configuration.
     */
    public WebServerBuilderWithRouter router(final Router router) {
        return new WebServerBuilderWithRouter(router);
    }

    /**
     * Configures the web server to use a parameterizable router.
     *
     * @return A new instance of WebServerBuilderWithRouter for further configuration.
     */
    public WebServerBuilderWithRouter parameterized() {
        return new WebServerBuilderWithRouter(new ParameterizableRouter());
    }

    /**
     * Configures the web server to use a simple static router.
     *
     * @return A new instance of WebServerBuilderWithRouter for further configuration.
     */
    public WebServerBuilderWithRouter simple() {
        return new WebServerBuilderWithRouter(new StaticRouter());
    }

    /**
     * Final-Stage-Builder for configuring a WebServer with a specific Router.
     * This class allows for detailed configuration of client handlers, error handlers, and routes.
     */
    public class WebServerBuilderWithRouter {
        private final Router router;

        private ClientHandler clientHandler = null;
        private ErrorHandler errorHandler = null;

        private WebServerBuilderWithRouter(final Router router) {
            this.router = router;
        }

        /**
         * Sets the error handler for the web server.
         *
         * @param errorHandler The error handler to use for processing any exceptions that occur during request handling.
         * @return This instance of WebServerBuilderWithRouter for method chaining.
         */
        public WebServerBuilderWithRouter errorHandler(final ErrorHandler errorHandler) {
            this.errorHandler = errorHandler;
            return this;
        }

        /**
         * Provides a default error handler if none is specified. This handler responds with an internal server error status.
         *
         * @return The default error handler.
         */
        private ErrorHandler defaultErrorHandler() {
            return (req, throwable) -> new ResponseBuilder()
                    .status(StatusCode.INTERNAL_SERVER_ERROR)
                    .text("Error: " + throwable.getMessage())
                    .build();
        }

        /**
         * Sets the client handler for the web server.
         *
         * @param clientHandler The client handler to use for processing incoming requests.
         * @return This instance of WebServerBuilderWithRouter for method chaining.
         */
        public WebServerBuilderWithRouter clientHandler(final ClientHandler clientHandler) {
            this.clientHandler = clientHandler;
            return this;
        }

        /**
         * Provides a default client handler if none is specified, using the configured router and error handler.
         *
         * @return The default client handler.
         */
        private ClientHandler defaultClientHandler() {
            final ErrorHandler serverErrorHandler = this.errorHandler != null
                    ? this.errorHandler
                    : this.defaultErrorHandler();
            return new BasicRouterClientHandler(this.router, serverErrorHandler);
        }


        public WebServerBuilderWithRouter register(final Method method, final String path, final Handler handler) {
            this.router.registerRoute(method, path, handler);
            return this;
        }

        public WebServerBuilderWithRouter register(final Method method,
                                                   final String path,
                                                   final Function<Context, ResponseLike> handlerBuilder) {
            return this.register(method, path, Handler.wrap(handlerBuilder));
        }

        public WebServerBuilderWithRouter get(final String path,
                                              final Function<Context, ResponseLike> handlerBuilder) {
            return this.register(Method.GET, path, handlerBuilder);
        }

        public WebServerBuilderWithRouter post(final String path,
                                               final Function<Context, ResponseLike> handlerBuilder) {
            return this.register(Method.POST, path, handlerBuilder);
        }

        public WebServerBuilderWithRouter put(final String path,
                                              final Function<Context, ResponseLike> handlerBuilder) {
            return this.register(Method.PUT, path, handlerBuilder);
        }

        public WebServerBuilderWithRouter delete(final String path,
                                                 final Function<Context, ResponseLike> handlerBuilder) {
            return this.register(Method.DELETE, path, handlerBuilder);
        }

        public WebServerBuilderWithRouter patch(final String path,
                                                final Function<Context, ResponseLike> handlerBuilder) {
            return this.register(Method.PATCH, path, handlerBuilder);
        }

        /**
         * Builds the WebServer using the configured router and handlers.
         *
         * @return The constructed WebServer.
         */
        public WebServer build() {
            final ClientHandler serverClientHandler = this.clientHandler != null
                    ? this.clientHandler
                    : this.defaultClientHandler();

            return new WebServer(WebServerBuilder.this.port, serverClientHandler);
        }
    }

}
