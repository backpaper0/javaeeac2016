package com.example;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.client.oauth2.OAuth2ClientSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
public class OAuth2ClientFactory {

    @Value("${clientId}")
    private String clientId;
    @Value("${clientSecret}")
    private String clientSecret;

    @Autowired
    private AccessToken accessToken;

    @Bean
    @RequestScope(proxyMode = ScopedProxyMode.INTERFACES)
    public Client client() {
        return ClientBuilder.newBuilder()
                .register(HttpAuthenticationFeature.basic(clientId, clientSecret))
                .register(OAuth2ClientSupport.feature(accessToken.getValue()))
                .build();
    }
}
