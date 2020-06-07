package com.example;

import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.atomic.AtomicLong;

@Path("/hello-world")
public class HelloWorldResource {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Greeting sayHello(@QueryParam("name") @DefaultValue("Stranger") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
}