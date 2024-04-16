package com.github.pokee.stick.response.writers;

import com.github.pokee.stick.response.Response;

import java.io.BufferedWriter;
import java.io.IOException;

public interface ResponseWriter {

    void write(final Response response, final BufferedWriter writer) throws IOException;

}
