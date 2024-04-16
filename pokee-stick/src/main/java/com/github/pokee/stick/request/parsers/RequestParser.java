package com.github.pokee.stick.request.parsers;

import com.github.pokee.stick.request.Request;
import com.github.pokee.stick.request.RequestHeader;

import java.io.BufferedReader;
import java.io.IOException;

public interface RequestParser {

    Request parse(final RequestHeader header, final BufferedReader reader) throws IOException;

}
