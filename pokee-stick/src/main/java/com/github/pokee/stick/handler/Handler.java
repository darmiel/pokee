package com.github.pokee.stick.handler;

import com.github.pokee.stick.request.Request;
import com.github.pokee.stick.response.Response;

public interface Handler {

    Response handle(final Request request);

}
