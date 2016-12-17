package com.example;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Path("")
public class HelloWorld {

    @Value("${userUri}")
    private String userUri;

    @Autowired
    private Client client;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String home() {
        String json = client
                .target(userUri)
                .request()
                .get(String.class);
        JsonObject root = Json.createReader(new StringReader(json)).readObject();
        JsonObject principal = root.getJsonObject("principal");
        String username = principal.getString("username");
        return String.format("Hello, %s!", username);
    }
}
