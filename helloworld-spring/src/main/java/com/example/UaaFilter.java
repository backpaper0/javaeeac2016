package com.example;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

import javax.ws.rs.client.Client;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.client.oauth2.ClientIdentifier;
import org.glassfish.jersey.client.oauth2.OAuth2ClientSupport;
import org.glassfish.jersey.client.oauth2.OAuth2CodeGrantFlow;
import org.glassfish.jersey.client.oauth2.OAuth2CodeGrantFlow.Phase;
import org.glassfish.jersey.client.oauth2.OAuth2Parameters;
import org.glassfish.jersey.client.oauth2.TokenResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Provider
public class UaaFilter implements ContainerRequestFilter {

    @Value("${clientId}")
    private String clientId;
    @Value("${clientSecret}")
    private String clientSecret;
    @Value("${authorizationUri}")
    private String authorizationUri;
    @Value("${accessTokenUri}")
    private String accessTokenUri;
    @Value("${redirectUri}")
    private String redirectUri;

    @Autowired
    private AccessToken accessToken;
    @Autowired
    private State state;
    @Autowired
    private Client client;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (accessToken.getValue() == null) {

            MultivaluedMap<String, String> queryParameters = requestContext.getUriInfo()
                    .getQueryParameters();

            String code = queryParameters.getFirst("code");
            if (code != null) {
                ClientIdentifier clientIdentifier = new ClientIdentifier(clientId, clientSecret);
                OAuth2CodeGrantFlow flow = OAuth2ClientSupport.authorizationCodeGrantFlowBuilder(
                        clientIdentifier, authorizationUri, accessTokenUri)
                        .redirectUri(redirectUri)
                        .client(client)
                        .property(Phase.ALL, OAuth2Parameters.STATE, state.getValue())
                        .build();
                String state = queryParameters.getFirst("state");
                TokenResult tokenResult = flow.finish(code, state);
                accessToken.setValue(tokenResult.getAccessToken());

            } else {
                state.setValue(UUID.randomUUID().toString());
                ClientIdentifier clientIdentifier = new ClientIdentifier(clientId, clientSecret);
                OAuth2CodeGrantFlow flow = OAuth2ClientSupport.authorizationCodeGrantFlowBuilder(
                        clientIdentifier, authorizationUri, accessTokenUri)
                        .redirectUri(redirectUri)
                        .client(client)
                        .property(Phase.ALL, OAuth2Parameters.STATE, state.getValue())
                        .build();
                URI location = URI.create(flow.start());
                Response response = Response.temporaryRedirect(location).build();
                requestContext.abortWith(response);
            }
        }
    }
}
