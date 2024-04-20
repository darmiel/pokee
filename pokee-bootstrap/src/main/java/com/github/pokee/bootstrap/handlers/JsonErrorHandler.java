package com.github.pokee.bootstrap.handlers;

import com.github.pokee.pson.mapper.annotations.JsonProperty;
import com.github.pokee.pswf.request.Request;
import com.github.pokee.pswf.response.Response;
import com.github.pokee.pswf.response.ResponseBuilder;
import com.github.pokee.pswf.response.StatusCode;
import com.github.pokee.pswf.router.ErrorHandler;

/**
 * An error handler that returns a JSON response.
 */
public class JsonErrorHandler implements ErrorHandler {

    @Override
    public Response handle(final Request request, final Throwable throwable) {
        return new ResponseBuilder()
                .status(StatusCode.INTERNAL_SERVER_ERROR)
                .json(new Error(throwable.getMessage()))
                .build();
    }

    /**
     * Represents an error response.
     *
     * @param errorMessage the error message
     */
    public record Error(@JsonProperty("error") String errorMessage) {
    }

}
