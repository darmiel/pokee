package com.github.pokee.stick.connection;

import java.io.IOException;
import java.net.Socket;

public interface ClientHandler {

    void handle(final Socket socket) throws IOException;

}
