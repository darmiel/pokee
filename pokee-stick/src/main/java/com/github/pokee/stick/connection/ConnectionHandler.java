package com.github.pokee.stick.connection;

import com.github.pokee.stick.StatusCode;
import com.github.pokee.stick.handler.Handler;
import com.github.pokee.stick.request.Request;
import com.github.pokee.stick.response.Response;
import com.github.pokee.stick.response.ResponseBuilder;
import com.github.pokee.stick.response.writers.ResponseWriter;

import java.io.*;
import java.net.Socket;

public class ConnectionHandler extends Thread {

    private final WebServer server; // TODO: replace with router
    private final Socket socket;

    public ConnectionHandler(final Socket socket, final WebServer server) {
        this.socket = socket;
        this.server = server;
    }

    private void handle() throws IOException {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
             final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()))) {

            final Request request = Request.readRequest(reader);
            System.out.println(request.prettyPrint());

            // TODO: middlewares etc.

            final Response response;
            final Handler handler = this.server.getHandler(request.path());
            if (handler == null) {
                response = new ResponseBuilder()
                        .status(StatusCode.NOT_FOUND)
                        .text("Cannot " + request.method() + " " + request.path())
                        .build();
            } else {
                response = handler.handle(request);
            }

            final ResponseWriter responseWriter = request.version().getWriter();
            if (responseWriter == null) {
                throw new UnsupportedOperationException("Unsupported version for writing: " + request.version());
            }

            System.out.println("Response: " + response);

            responseWriter.write(response, writer);
            writer.newLine();
            writer.flush();
        } catch (final IOException exception) {
            System.out.println("Error communicating with the client: " + exception.getMessage());
        } finally {
            socket.close();
            System.out.println("Bye!");
        }
    }

    @Override
    public void run() {
        try {
            this.handle();
        } catch (final IOException exception) {
            System.out.println("Error handling connection: " + exception.getMessage());
        }
    }

}
