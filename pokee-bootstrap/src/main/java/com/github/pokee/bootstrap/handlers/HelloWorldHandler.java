package com.github.pokee.bootstrap.handlers;

import com.github.pokee.pson.mapper.annotations.JsonProperty;
import com.github.pokee.pswf.annotation.RoutePrefix;
import com.github.pokee.pswf.annotation.data.Param;
import com.github.pokee.pswf.annotation.data.Query;
import com.github.pokee.pswf.annotation.generator.ContentType;
import com.github.pokee.pswf.annotation.method.GET;
import com.github.pokee.pswf.response.Response;
import com.github.pokee.pswf.response.ResponseBuilder;
import com.github.pokee.pswf.response.ResponseLike;
import com.github.pokee.pswf.response.StatusCode;
import com.github.pokee.pswf.util.ContentTypes;

import java.util.List;

@RoutePrefix("/hello")
public class HelloWorldHandler {

    /**
     * A simple hello world endpoint.
     *
     * @return A simple hello world message.
     */
    @GET("/")
    public String sayHello() {
        return "Hello World!";
    }

    ///

    /**
     * A simple hello world endpoint that serializes the response to JSON
     *
     * @return A simple hello world message.
     */
    @GET("/:name")
    @ContentType(ContentTypes.JSON) // this is optional, automatically sets the content type
    public HelloResponse sayHello(@Param("name") final String name,
                                  @Query("age") final int age) {
        return new HelloResponse(name, age);
    }

    /**
     * A simple hello world endpoint that serializes the response to JSON
     *
     * @return A simple hello world message.
     */
    @GET("/:name/multi")
    public List<HelloResponse> sayHelloMulti(@Param("name") final String name) {
        return List.of(new HelloResponse(name, 0), new HelloResponse(name, 0));
    }

    public record HelloResponse(@JsonProperty("Hello") String target, @JsonProperty("Age") int age) {
    }

    ///

    /**
     * A simple hello world endpoint that returns a raw response
     *
     * @return A simple hello world message.
     */
    @GET("/me")
    public Response sayHelloMe() {
        return new ResponseBuilder()
                .status(StatusCode.ACCEPTED)
                .text("Hello Me!")
                .build();
    }

    ///

    /**
     * A simple hello world endpoint that gets the response from {@link ResponseLike}
     *
     * @return A simple hello world message.
     */
    @GET("/world")
    public HelloWorldResponse sayHelloWorld() {
        return new HelloWorldResponse();
    }

    public record HelloWorldResponse() implements ResponseLike {
        @Override
        public Response extractResponse() {
            return new ResponseBuilder()
                    .status(StatusCode.ACCEPTED)
                    .text("Hello World!")
                    .build();
        }
    }


}
