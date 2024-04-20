package com.github.pokee.pswf.response;

import com.github.pokee.pson.Pson;
import com.github.pokee.pson.mapper.mappers.UUIDMapper;
import com.github.pokee.pswf.headers.Headers;
import com.github.pokee.pswf.util.ContentTypes;

import java.util.UUID;

/**
 * A builder class for creating {@link Response} objects, allowing for easy setting of
 * HTTP status codes, headers, and the body content.
 */
public class ResponseBuilder implements ResponseLike {

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

    /**
     * Constructs a new ResponseBuilder, defaulting the status code to 200 OK.
     */
    public ResponseBuilder() {
        this.headers = new Headers();

        this.status(StatusCode.OK); // default to 200 OK
    }

    /**
     * Sets the status code and message of the response. Note that the status message is not
     * the body of the response, but rather the status message returned in the HTTP header, e.g.:
     * <pre>
     *     HTTP/1.1 200 OK
     *                  ^^
     * </pre>
     *
     * @param statusCode    the HTTP status code
     * @param statusMessage the HTTP status message
     * @return this builder instance for chaining
     */
    public ResponseBuilder status(final int statusCode, final String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        return this;
    }

    /**
     * Sets the status code of the response with an empty status message.
     *
     * @param statusCode the HTTP status code
     * @return this builder instance for chaining
     */
    public ResponseBuilder status(final int statusCode) {
        return this.status(statusCode, "");
    }


    /**
     * Sets the status code and message based on a {@link StatusCode} enum.
     *
     * @param statusCode the HTTP status code as a {@link StatusCode} enum
     * @return this builder instance for chaining
     */
    public ResponseBuilder status(final StatusCode statusCode) {
        return this.status(statusCode.code(), statusCode.description());
    }

    /**
     * Adds a header to the response.
     *
     * @param key   the header name
     * @param value the header value
     * @return this builder instance for chaining
     */
    public ResponseBuilder add(final String key, final String value) {
        this.headers.add(key, value);
        return this;
    }

    /**
     * Sets a header in the response, replacing any existing value for the header.
     *
     * @param key   the header name
     * @param value the header value
     * @return this builder instance for chaining
     */
    public ResponseBuilder set(final String key, final String value) {
        this.headers.set(key, value);
        return this;
    }

    /**
     * Sets the content type of the response.
     *
     * @param contentType the MIME type to set as the content type
     * @return this builder instance for chaining
     */
    public ResponseBuilder contentType(final String contentType) {
        return this.set(CONTENT_TYPE_HEADER, contentType);
    }

    /**
     * Sets the body of the response.
     *
     * @param body the body as a byte array
     * @return this builder instance for chaining
     */
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
        return this.contentType(ContentTypes.TEXT).body(textBody.getBytes());
    }

    /**
     * Set the body of the response to the given HTML string.
     *
     * @param htmlBody the HTML body
     * @return the response builder
     */
    public ResponseBuilder html(final String htmlBody) {
        return this.contentType(ContentTypes.HTML).body(htmlBody.getBytes());
    }

    /**
     * Set the body of the response to the given JSON string.
     *
     * @param jsonBody the JSON body
     * @return the response builder
     */
    public ResponseBuilder json(final String jsonBody) {
        return this.contentType(ContentTypes.JSON).body(jsonBody.getBytes());
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
     * Builds the response object using the current settings.
     *
     * @return the constructed {@link Response}
     */
    public Response build() {
        return new Response(this.statusCode, this.statusMessage, this.headers, this.body);
    }

    @Override
    public Response extractResponse() {
        return this.build();
    }

}
