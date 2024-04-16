package com.github.pokee.stick.connection;

import com.github.pokee.stick.handler.Handler;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

public class WebServer {

    private final int port;
    private final Map<String, Handler> handlerMap;

    private ServerSocket serverSocket;

    public WebServer(final int port) {
        this.port = port;
        this.handlerMap = new HashMap<>();
    }

    public void GET(final String path, final Handler handler) {
        this.handlerMap.put(path, handler);
    }

    /**
     * Start the server.
     *
     * @throws IOException if an I/O error occurs when creating the server socket.
     */
    public void start() throws IOException {
        this.serverSocket = new ServerSocket(this.port);

        while (true) {
            new ConnectionHandler(this.serverSocket.accept(), this).start();
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

    public Handler getHandler(final String path) {
        return this.handlerMap.get(path);
    }

}
