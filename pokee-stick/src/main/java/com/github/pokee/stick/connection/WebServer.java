package com.github.pokee.stick.connection;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Represents a simple HTTP web server. This class is responsible for opening a server socket
 * on a specified port and handling incoming client connections by delegating them to a
 * {@link ClientHandler}.
 */
public class WebServer {

    private final int port;

    private final ClientHandler clientHandler;
    private ServerSocket serverSocket;

    /**
     * Constructs a new WebServer instance.
     *
     * @param port          The port number on which the server will listen for incoming connections.
     * @param clientHandler The handler that will process client connections.
     */
    public WebServer(final int port, final ClientHandler clientHandler) {
        this.port = port;
        this.clientHandler = clientHandler;
    }

    /**
     * Starts the server and begins listening for incoming connections. This method creates
     * a server socket on the specified port and continuously accepts new client connections.
     * Each connection is handled in its own thread using {@link ThreadedClientHandler}.
     *
     * @throws IOException If an I/O error occurs when opening the server socket.
     */
    public void start() throws IOException {
        this.serverSocket = new ServerSocket(this.port);

        while (true) {
            new ThreadedClientHandler(this.serverSocket.accept(), this.clientHandler).start();
        }
    }

    /**
     * Stops the server by closing the server socket. This method should be called to release
     * the resources associated with the server socket and to ensure that the server stops
     * accepting new connections.
     *
     * @throws IOException If an I/O error occurs when closing the server socket.
     */
    public void stop() throws IOException {
        this.serverSocket.close();
    }

}
