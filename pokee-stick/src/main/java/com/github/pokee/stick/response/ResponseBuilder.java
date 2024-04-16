package com.github.pokee.stick.response;

import com.github.pokee.stick.headers.Headers;
import com.github.pokee.stick.ContentType;
import com.github.pokee.stick.StatusCode;

public class ResponseBuilder {

    public static final String CONTENT_TYPE_HEADER = "Content-Type";
    public static final String CONTENT_LENGTH_HEADER = "Content-Length";

    private final Headers headers = new Headers();
    private final ResponsePsonProxy psonProxy = new ResponsePsonProxy();

    private int statusCode;
    private String statusMessage;
    private String body;

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

    public ResponseBuilder body(final String body) {
        this.body = body;
        return this.set(CONTENT_LENGTH_HEADER, String.valueOf(body.length() + 1));
    }

    /**
     * Set the body of the response to the given HTML string.
     *
     * @param htmlBody the HTML body
     * @return the response builder
     */
    public ResponseBuilder html(final String htmlBody) {
        return this.contentType(ContentType.HTML).body(htmlBody);
    }

    /**
     * Set the body of the response to the given JSON string.
     *
     * @param jsonBody the JSON body
     * @return the response builder
     */
    public ResponseBuilder json(final String jsonBody) {
        return this.contentType(ContentType.JSON).body(jsonBody);
    }

    /**
     * Set the body of the response to the given object, serialized to JSON.
     *
     * @param object the object to serialize
     * @return the response builder
     */
    public ResponseBuilder json(final Object object) {
        return this.json(this.toJson(object));
    }

    /**
     * Set the body of the response to the given plain text string.
     *
     * @param textBody the text body
     * @return the response builder
     */
    public ResponseBuilder text(final String textBody) {
        return this.contentType(ContentType.TEXT).body(textBody);
    }

    /**
     * Build the response.
     */
    public Response build() {
        return new Response(this.statusCode, this.statusMessage, this.headers, this.body);
    }

    protected String toJson(final Object object) {
        return this.psonProxy.marshal(object);
    }

}
