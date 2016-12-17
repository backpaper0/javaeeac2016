package com.example;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

@Dependent
public class OAuth2Properties {

    @Named("clientId")
    @Produces
    public String clientId() {
        return "sample";
    }

    @Named("clientSecret")
    @Produces
    public String clientSecret() {
        return "secret";
    }

    @Named("authorizationUri")
    @Produces
    public String authorizationUri() {
        return "http://localhost:9999/uaa/oauth/authorize";
    }

    @Named("accessTokenUri")
    @Produces
    public String accessTokenUri() {
        return "http://localhost:9999/uaa/oauth/token";
    }

    @Named("redirectUri")
    @Produces
    public String redirectUri() {
        return "http://localhost:8080/helloworld/";
    }

    @Named("userUri")
    @Produces
    public String userUrl() {
        return "http://localhost:9999/uaa/user";
    }
}
