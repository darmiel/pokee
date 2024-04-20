package com.github.pokee.stick.response;

/**
 * Defines an interface for objects that can be converted to a {@link Response} object.
 */
public interface ResponseLike {

    /**
     * Extracts a {@link Response} object from this instance.
     *
     * @return The Response object represented by this instance.
     */
    Response extractResponse();

}
