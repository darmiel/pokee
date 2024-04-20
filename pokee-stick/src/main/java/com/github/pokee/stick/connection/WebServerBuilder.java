package com.github.pokee.stick.connection;

import com.github.pokee.stick.Method;
import com.github.pokee.stick.StatusCode;
import com.github.pokee.stick.response.ResponseBuilder;
import com.github.pokee.stick.response.ResponseLike;
import com.github.pokee.stick.router.*;

import java.util.function.Function;

public class WebServerBuilder {

    private final int port;

    public WebServerBuilder(int port) {
        this.port = port;
    }

    public WebServerBuilderWithRouter router(final Router router) {
        return new WebServerBuilderWithRouter(router);
    }

    public WebServerBuilderWithRouter parameterized() {
        return new WebServerBuilderWithRouter(new ParameterizableRouter());
    }

    public WebServerBuilderWithRouter simple() {
        return new WebServerBuilderWithRouter(new StaticRouter());
    }

    public class WebServerBuilderWithRouter {
        private final Router router;

        private ClientHandler clientHandler = null;
        private ErrorHandler errorHandler = null;

        private WebServerBuilderWithRouter(final Router router) {
            this.router = router;
        }

        public WebServerBuilderWithRouter errorHandler(final ErrorHandler errorHandler) {
            this.errorHandler = errorHandler;
            return this;
        }

        private ErrorHandler defaultErrorHandler() {
            return (req, throwable) -> new ResponseBuilder()
                    .status(StatusCode.INTERNAL_SERVER_ERROR)
                    .text("Error: " + throwable.getMessage())
                    .build();
        }

        public WebServerBuilderWithRouter clientHandler(final ClientHandler clientHandler) {
            this.clientHandler = clientHandler;
            return this;
        }

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


        public WebServer build() {
            final ClientHandler serverClientHandler = this.clientHandler != null
                    ? this.clientHandler
                    : this.defaultClientHandler();

            return new WebServer(WebServerBuilder.this.port, serverClientHandler);
        }
    }

}
