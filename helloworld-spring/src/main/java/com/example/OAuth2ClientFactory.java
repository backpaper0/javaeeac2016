package com.example;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.client.oauth2.OAuth2ClientSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@Component
public class OAuth2ClientFactory {

    @Value("${clientId}")
    private String clientId;
    @Value("${clientSecret}")
    private String clientSecret;

    @Autowired
    private AccessToken accessToken;

    @Bean
    @ApplicationScope
    public Client client() {
        return ClientBuilder.newBuilder()
                .register(HttpAuthenticationFeature.basic(clientId, clientSecret))
                .register(new AccessTokenRegister())
                .register(OAuth2ClientSupport.feature(accessToken.getValue()))
                .build();
    }

    @Priority(0)
    public class AccessTokenRegister implements ClientRequestFilter {
        @Override
        public void filter(ClientRequestContext requestContext) throws IOException {
            requestContext.setProperty(
                    OAuth2ClientSupport.OAUTH2_PROPERTY_ACCESS_TOKEN,
                    accessToken.getValue());
        }
    }
}
