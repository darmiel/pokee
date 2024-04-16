package com.github.pokee.stick.response;

import com.github.pokee.pson.Pson;
import com.github.pokee.pson.mapper.mappers.UUIDMapper;
import com.github.pokee.stick.ContentType;
import com.github.pokee.stick.StatusCode;
import com.github.pokee.stick.headers.Headers;

import java.util.UUID;

public class ResponseBuilder {

    public static final Pson PSON = Pson.create()
            .prettyPrint()
            .expandFunctions(false)
            .registerMapper(UUID.class, UUIDMapper.INSTANCE)
            .build();

    public static final String CONTENT_TYPE_HEADER = "Content-Type";
    public static final String CONTENT_LENGTH_HEADER = "Content-Length";

    private final Headers headers;

    private int statusCode;
    private String statusMessage;
    private byte[] body;

    public ResponseBuilder() {
        this.headers = new Headers();

        this.status(StatusCode.OK); // default to 200 OK
    }

    public ResponseBuilder status(final int statusCode, final String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        return this;
    }

    public ResponseBuilder status(final int statusCode) {
        return this.status(statusCode, "");
    }

    public ResponseBuilder status(final StatusCode statusCode) {
        return this.status(statusCode.code(), statusCode.description());
    }

    public ResponseBuilder add(final String key, final String value) {
        this.headers.add(key, value);
        return this;
    }

    public ResponseBuilder set(final String key, final String value) {
        this.headers.set(key, value);
        return this;
    }

    public ResponseBuilder contentType(final String contentType) {
        return this.set(CONTENT_TYPE_HEADER, contentType);
    }

    public ResponseBuilder body(final byte[] body) {
        this.body = body;
        return this.set(CONTENT_LENGTH_HEADER, String.valueOf(body.length));
    }

    /**
     * Set the body of the response to the given plain text string.
     *
     * @param textBody the text body
     * @return the response builder
     */
    public ResponseBuilder text(final String textBody) {
        return this.contentType(ContentType.TEXT).body(textBody.getBytes());
    }

    /**
     * Set the body of the response to the given HTML string.
     *
     * @param htmlBody the HTML body
     * @return the response builder
     */
    public ResponseBuilder html(final String htmlBody) {
        return this.contentType(ContentType.HTML).body(htmlBody.getBytes());
    }

    /**
     * Set the body of the response to the given JSON string.
     *
     * @param jsonBody the JSON body
     * @return the response builder
     */
    public ResponseBuilder json(final String jsonBody) {
        return this.contentType(ContentType.JSON).body(jsonBody.getBytes());
    }

    /**
     * Set the body of the response to the given object, serialized to JSON.
     *
     * @param object the object to serialize
     * @return the response builder
     */
    public ResponseBuilder json(final Object object) {
        return this.json(ResponseBuilder.PSON.marshal(object));
    }

    /**
     * Build the response.
     */
    public Response build() {
        return new Response(this.statusCode, this.statusMessage, this.headers, this.body);
    }
}
