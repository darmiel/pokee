package com.github.pokee.stick.connection.handler;

import java.io.IOException;
import java.net.Socket;

/**
 * A thread that handles an individual client connection. This class extends {@link Thread}
 * and is responsible for managing the lifecycle of a client connection from acceptance to closure,
 * delegating the processing of the client's requests to a specified {@link ClientHandler}.
 */
public class ThreadedClientHandler extends Thread {

    private final Socket clientSocket;  // The client socket this thread is responsible for.
    private final ClientHandler clientHandler;  // The handler that processes the client's requests.

    /**
     * Constructs a new ThreadedClientHandler for handling a specific client socket.
     *
     * @param clientSocket  The socket of the client to handle.
     * @param clientHandler The handler that will process the requests from the client.
     */
    public ThreadedClientHandler(final Socket clientSocket, final ClientHandler clientHandler) {
        this.clientSocket = clientSocket;
        this.clientHandler = clientHandler;
    }

    /**
     * Runs the handler in a separate thread. This method is invoked when the thread is started,
     * and it delegates to the clientHandler to process the client's requests. If an IOException
     * occurs during processing, it logs an error message to the standard output.
     */
    @Override
    public void run() {
        try {
            this.clientHandler.handle(this.clientSocket);
        } catch (final IOException exception) {
            System.out.println("Error communicating with the client: " + exception.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Error closing client socket: " + e.getMessage());
            }
        }
    }

}
