package com.github.pokee.stick.connection;

import com.github.pokee.stick.StatusCode;
import com.github.pokee.stick.request.Request;
import com.github.pokee.stick.response.Response;
import com.github.pokee.stick.response.ResponseBuilder;
import com.github.pokee.stick.response.writers.ResponseWriter;
import com.github.pokee.stick.router.Context;
import com.github.pokee.stick.router.Handler;
import com.github.pokee.stick.router.ParameterizableRouter;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

public class BasicRouterClientHandler implements ClientHandler {

    private final ParameterizableRouter router;

    public BasicRouterClientHandler(ParameterizableRouter router) {
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

            final Context context = this.router.createContext(request);
            final List<Handler> handlers = context.getHandlers();

            Response response;
            if (!handlers.isEmpty()) {
                for (final Handler handler : handlers) {
                    handler.handle(context);
                }
                response = context.getResponse();
            } else {
                response = new ResponseBuilder()
                        .status(StatusCode.NOT_FOUND)
                        .text("Cannot " + request.method() + " " + request.path())
                        .build();
            }

            // if the handler didn't return a response, return a 204 No Content
            if (response == null) {
                response = new ResponseBuilder()
                        .status(StatusCode.NO_CONTENT)
                        .text("No content returned")
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
