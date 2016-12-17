package com.example;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("")
public class HelloWorld {

    @Inject
    @Named("userUri")
    private String userUri;

    @Inject
    private Client client;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String home() {
        JsonObject root = client
                .target(userUri)
                .request()
                .get(JsonObject.class);
        JsonObject principal = root.getJsonObject("principal");
        String username = principal.getString("username");
        return String.format("Hello, %s!", username);
    }
}
