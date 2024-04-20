package com.github.pokee.bootstrap.config;

import com.github.pokee.pson.mapper.annotations.PsonSource;

@PsonSource(file = "server.pson")
public class WebServerConfig {

    private final int port;

    public WebServerConfig(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "WebServerConfig{" +
                "port=" + port +
                '}';
    }

}
