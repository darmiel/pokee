# PSWF - Pokee Web Framework

PSWF (_Pokee's ~~Shitty~~ Web Framework_) is a minimalistic and incomplete Web Server and -Framework.
It provides basic functionalities to handle incoming requests.

## Basic Usage

### Creating a Web Server

To create a web server, use the `WebServerBuilder` class. Here’s how you can start a server on port 8080:

```java
public static void main(String[] args) {
    final WebServer server = new WebServerBuilder(8080)
            .simple() // Use the simple router
            .build();

    server.start();
}
```

### Defining Routes Using Annotations

To define routes using annotations, create handler classes annotated with HTTP method annotations such
as `@GET`, `@POST`, etc.
Here's an example handler:

```java
package com.github.pokee.bootstrap.handlers;

import com.github.pokee.pswf.annotation.RoutePrefix;
import com.github.pokee.pswf.annotation.method.GET;
import com.github.pokee.pswf.response.Response;
import com.github.pokee.pswf.response.ResponseBuilder;

@RoutePrefix("/hello")
public class HelloWorldHandler {

    @GET("/")
    public Response sayHello() {
        return new ResponseBuilder()
                .text("Hello, World!")
                .build();
    }

}
```

To use this handler, register it with the server using the `clazz` method from the `WebServerBuilder`:

```java
final WebServer server = new WebServerBuilder(8080)
        .parameterized()
        .clazz(new HelloWorldHandler())
        .build();
```

### Handling Parameters and Queries

Handle dynamic URL parameters and queries directly in your handler methods by using the `@Param` and `@Query`
annotations:

```java
import com.github.pokee.pswf.annotation.data.Param;
import com.github.pokee.pswf.annotation.data.Query;
import com.github.pokee.pswf.response.ResponseBuilder;

@GET("/hello/:name")
public Response personalizedGreeting(final @Param("name") String name,
                                     final @Query("greet") String greeting) {
    String responseText = greeting + ", " + name + "!";
    return new ResponseBuilder()
            .text(responseText)
            .build();
}
```

### Returning JSON

To return JSON from a handler, just return any object.

```java
import com.github.pokee.pswf.annotation.method.GET;
import com.github.pokee.pson.Pson;
import com.github.pokee.pswf.response.ResponseBuilder;

public class DataHandler {

    private record HelloWorldResponse(String message, int number) {
    }

    @GET("/data")
    public HelloWorldResponse getData() {
        return new HelloWorldResponse("Hello, World!", 123);
    }
}
```

### Error Handling

Customize error handling using the `errorHandler` method:

```java
final WebServer server = new WebServerBuilder(8080)
        .parameterized()
        .errorHandler((request, exception) -> {
            return new ResponseBuilder()
                    .status(StatusCode.INTERNAL_SERVER_ERROR)
                    .text("An error occurred: " + exception.getMessage())
                    .build();
        });
```

## Limitations

This is just a proof of concept web framework and should _not_ be used in production.
It is not secure, not optimized, not feature-complete, not tested, not maintained, not documented, and not suitable for
any real-world use case.
It is just a fun project to learn more about web frameworks and how they work.
