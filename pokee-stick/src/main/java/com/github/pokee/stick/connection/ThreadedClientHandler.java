package com.github.pokee.stick.connection;

import java.io.IOException;
import java.net.Socket;

public class ThreadedClientHandler extends Thread {

    private final Socket clientSocket;
    private final ClientHandler clientHandler;

    public ThreadedClientHandler(final Socket clientSocket, final ClientHandler clientHandler) {
        this.clientSocket = clientSocket;
        this.clientHandler = clientHandler;
    }

    @Override
    public void run() {
        try {
            this.clientHandler.handle(this.clientSocket);
        } catch (final IOException exception) {
            System.out.println("Error communicating with the client: " + exception.getMessage());
        }
    }

}
