package com.github.pokee.stick;

/**
 * Enumerates HTTP status codes used in server responses, providing both the numeric
 * status code and its textual description.
 */
public enum StatusCode {
    OK(200, "OK"),
    CREATED(201, "Created"),
    ACCEPTED(202, "Accepted"),
    NO_CONTENT(204, "No Content"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    NOT_IMPLEMENTED(501, "Not Implemented"),
    BAD_GATEWAY(502, "Bad Gateway"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),
    GATEWAY_TIMEOUT(504, "Gateway Timeout");

    private final int code;
    private final String description;

    /**
     * Constructs a new status code enum constant with the specified numeric and textual representation.
     *
     * @param code        the numeric status code
     * @param description the textual description of the status code
     */
    StatusCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Returns the numeric status code.
     *
     * @return the numeric code of this status
     */
    public int code() {
        return this.code;
    }

    /**
     * Returns the textual description of the status code.
     *
     * @return the description of this status
     */
    public String description() {
        return this.description;
    }

}
