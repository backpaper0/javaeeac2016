package com.example;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.client.oauth2.OAuth2ClientSupport;

@Dependent
public class OAuth2ClientFactory {

    @Inject
    @Named("clientId")
    private String clientId;
    @Inject
    @Named("clientSecret")
    private String clientSecret;

    @Inject
    private AccessToken accessToken;

    @Produces
    public Client client() {
        return ClientBuilder.newBuilder()
                .register(HttpAuthenticationFeature.basic(clientId, clientSecret))
                .register(OAuth2ClientSupport.feature(accessToken.getValue()))
                .build();
    }
}
