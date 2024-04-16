package com.github.pokee.stick.connection;

import java.io.IOException;
import java.net.ServerSocket;

public class WebServer {

    private final int port;

    private final ClientHandler clientHandler;
    private ServerSocket serverSocket;

    public WebServer(final int port, final ClientHandler clientHandler) {
        this.port = port;
        this.clientHandler = clientHandler;
    }

    /**
     * Start the server.
     *
     * @throws IOException if an I/O error occurs when creating the server socket.
     */
    public void start() throws IOException {
        this.serverSocket = new ServerSocket(this.port);

        while (true) {
            new ThreadedClientHandler(this.serverSocket.accept(), this.clientHandler).start();
        }
    }

    /**
     * Stop the server.
     *
     * @throws IOException if an I/O error occurs when closing the server socket.
     */
    public void stop() throws IOException {
        this.serverSocket.close();
    }

}
