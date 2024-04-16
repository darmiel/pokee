package com.github.pokee.stick.connection;

import com.github.pokee.stick.StatusCode;
import com.github.pokee.stick.request.Request;
import com.github.pokee.stick.response.Response;
import com.github.pokee.stick.response.ResponseBuilder;
import com.github.pokee.stick.response.writers.ResponseWriter;
import com.github.pokee.stick.router.Handler;
import com.github.pokee.stick.router.Router;
import com.github.pokee.stick.router.RouterRunner;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class BasicRouterClientHandler implements ClientHandler {

    private final Router router;

    public BasicRouterClientHandler(Router router) {
        this.router = router;
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

            Response response = null;

            // TODO: middlewares etc.
            // find handlers for the request
            final RouterRunner runner = this.router.findRoutes(request.method(), request.path());
            for (Handler handler : runner.handlers()) {
                if ((response = handler.handle(request)) != null) { // TODO: change this in the future
                    System.out.println("Params for handler: " + runner.parameters());
                    break;
                }
            }

            if (response == null) {
                response = new ResponseBuilder()
                        .status(StatusCode.NOT_FOUND)
                        .text("Cannot " + request.method() + " " + request.path())
                        .build();
            }

            responseWriter.write(response, writer);
            writer.flush();
        } catch (final IOException exception) {
            System.out.println("Error communicating with the client: " + exception.getMessage());
        } finally {
            System.out.println("Bye!");
        }
    }

}
