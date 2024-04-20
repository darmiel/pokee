package com.github.pokee.stick.testing;

import com.github.pokee.stick.util.ContentTypes;
import com.github.pokee.stick.annotation.data.Body;
import com.github.pokee.stick.annotation.data.Param;
import com.github.pokee.stick.annotation.generator.ContentType;
import com.github.pokee.stick.annotation.method.GET;
import com.github.pokee.stick.annotation.method.POST;

public class RouteClass {

    @GET("/")
    public String getRoot() {
        return "Hello, World!";
    }

    @GET("/hello/:name")
    public String getHelloName(@Param("name") final String name) {
        return "Hello, " + name + "!";
    }

    @POST("/hello")
    @ContentType(ContentTypes.TEXT)
    public String postHello(@Body final String name) {
        return "Hello, " + name + "!";
    }

}
