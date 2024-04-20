package com.github.pokee.stick.connection;

import com.github.pokee.stick.exception.NoHandlerForRouteException;
import com.github.pokee.stick.exception.request.NoContentException;
import com.github.pokee.stick.request.Request;
import com.github.pokee.stick.response.Response;
import com.github.pokee.stick.response.writers.ResponseWriter;
import com.github.pokee.stick.router.Context;
import com.github.pokee.stick.router.ErrorHandler;
import com.github.pokee.stick.router.Handler;
import com.github.pokee.stick.router.Router;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

public class BasicRouterClientHandler implements ClientHandler {

    private final ErrorHandler errorHandler;

    private final Router router;

    public BasicRouterClientHandler(final Router router, final ErrorHandler errorHandler) {
        this.router = router;
        this.errorHandler = errorHandler;
    }

    private Response getResponseForContext(final Context context) throws NoContentException {
        final List<Handler> handlers = context.getHandlers();
        if (handlers.isEmpty()) {
            throw new NoHandlerForRouteException();
        }
        for (final Handler handler : handlers) {
            handler.handle(context);
        }
        final Response response = context.getResponse();
        if (response == null) {
            throw new NoContentException();
        }
        return response;
    }

    @Override
    public void handle(final Socket socket) throws IOException {
        try (socket;
             final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             final BufferedOutputStream writer = new BufferedOutputStream(socket.getOutputStream())) {

            final Request request = Request.readRequest(reader);
            System.out.println(request.prettyPrint());

            final ResponseWriter responseWriter = request.version().getWriter();
            if (responseWriter == null) {
                throw new UnsupportedOperationException("Unsupported version for writing: " + request.version());
            }

            Response response;
            try {
                final Context context = this.router.createContext(request);
                if ((response = this.getResponseForContext(context)) == null) {
                    response = this.errorHandler.handle(request, new NoHandlerForRouteException());
                }
            } catch (final Throwable throwable) {
                response = this.errorHandler.handle(request, throwable);
            }

            responseWriter.write(response, writer);
            writer.flush();
        } finally {
            System.out.println("Bye " + socket.getInetAddress());
        }
    }

}
