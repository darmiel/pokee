package com.github.pokee.stick.response.writers;

import com.github.pokee.stick.response.Response;
import com.github.pokee.stick.Version;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ResponseWriterVersion1_0 implements ResponseWriter {

    protected String getDelimiter() {
        return "\r\n";
    }

    protected Version getVersion() {
        return Version.VERSION_1_0;
    }

    protected void writeHeader(final Response response, final BufferedWriter writer) throws IOException {
        writer.write(this.getVersion() + " " + response.statusCode());
        if (response.statusMessage() != null && !response.statusMessage().isBlank()) {
            writer.write(" " + response.statusMessage());
        }
        writer.write(this.getDelimiter());
    }

    protected void writeResponseHeaders(final Response response, final BufferedWriter writer) throws IOException {
        for (final Map.Entry<String, List<String>> entry : response.headers().entries()) {
            for (final String value : entry.getValue()) {
                writer.write(entry.getKey() + ": " + value);
                writer.write(this.getDelimiter());
            }
        }
    }

    protected void writeContent(final Response response, final BufferedWriter writer) throws IOException {
        writer.write(this.getDelimiter());
        if (!response.body().isEmpty()) {
            writer.write(response.body());
        }
    }

    @Override
    public void write(final Response response, final BufferedWriter writer) throws IOException {
        this.writeHeader(response, writer);
        this.writeResponseHeaders(response, writer);
        this.writeContent(response, writer);
    }

}
