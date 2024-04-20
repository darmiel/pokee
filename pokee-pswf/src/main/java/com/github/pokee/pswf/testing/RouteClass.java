package com.github.pokee.pswf.testing;

import com.github.pokee.pswf.annotation.data.Body;
import com.github.pokee.pswf.annotation.data.Param;
import com.github.pokee.pswf.annotation.generator.ContentType;
import com.github.pokee.pswf.annotation.method.GET;
import com.github.pokee.pswf.annotation.method.POST;
import com.github.pokee.pswf.util.ContentTypes;

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
