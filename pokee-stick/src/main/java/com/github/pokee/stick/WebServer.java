package com.github.pokee.stick;

import com.github.pokee.pson.Pson;
import com.github.pokee.stick.request.Request;
import com.github.pokee.stick.response.Response;
import com.github.pokee.stick.response.ResponseBuilder;
import com.github.pokee.stick.response.writers.ResponseWriter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class WebServer {

    public record ResponseObject(UUID id, String name) {
    }

    public static void main(String[] args) throws IOException {
        final Pson pson = Pson.create()
                .serializeNulls()
                .prettyPrint()
                .registerValueReaderMapper(UUID.class, (element, field) -> UUID.fromString(element.asPrimitive().asString()))
                .registerValueWriterMapper(UUID.class, (writer, bob, field, value) -> writer.writeString(bob, value.toString())).build();

        final ServerSocket server = new ServerSocket(8080);

        while (true) {
            final Socket socket = server.accept();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

                final Request request = Request.readRequest(reader);
                System.out.println(request.prettyPrint());

                final ResponseWriter responseWriter = request.version().getWriter();
                if (responseWriter == null) {
                    throw new UnsupportedOperationException("Unsupported version for writing: " + request.version());
                }

                final Response response = new ResponseBuilder()
                        .status(StatusCode.CREATED)
                        .json(pson.marshal(new ResponseObject(UUID.randomUUID(), "pokee")))
                        .build();

                responseWriter.write(response, writer);
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                System.out.println("Error communicating with the client");
                e.printStackTrace();
            } finally {
                socket.close();
                System.out.println("Bye!");
            }
        }
    }

}
