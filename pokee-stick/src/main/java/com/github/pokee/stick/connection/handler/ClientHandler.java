package com.github.pokee.stick.connection.handler;

import java.io.IOException;
import java.net.Socket;

/**
 * Defines an interface for handling client connections. Implementers of this interface
 * are responsible for processing all activities related to a client's request once
 * a socket connection has been established.
 */
public interface ClientHandler {

    /**
     * Handles a client request via a given socket. This method is responsible for the entire
     * interaction with the client through the provided socket, including reading the request,
     * processing it, and sending back the response.
     *
     * @param socket The socket connection to the client that needs handling.
     * @throws IOException If an I/O error occurs during the handling of the request.
     */
    void handle(final Socket socket) throws IOException;

}
