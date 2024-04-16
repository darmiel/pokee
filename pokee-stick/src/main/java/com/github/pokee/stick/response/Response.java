package com.github.pokee.stick.response;

import com.github.pokee.stick.headers.Headers;

public record Response(int statusCode,
                       String statusMessage,
                       Headers headers,
                       String body) {


}
