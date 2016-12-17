package com.example;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;

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
public class UaaFilter implements Filter {

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
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (accessToken.getValue() != null) {
            chain.doFilter(request, response);
            return;
        }

        String code = request.getParameter("code");
        if (code != null) {
            ClientIdentifier clientIdentifier = new ClientIdentifier(clientId, clientSecret);
            OAuth2CodeGrantFlow flow = OAuth2ClientSupport.authorizationCodeGrantFlowBuilder(
                    clientIdentifier, authorizationUri, accessTokenUri)
                    .redirectUri(redirectUri)
                    .client(client)
                    .property(Phase.ALL, OAuth2Parameters.STATE, state.getValue())
                    .build();
            String state = request.getParameter("state");
            TokenResult tokenResult = flow.finish(code, state);
            accessToken.setValue(tokenResult.getAccessToken());
            chain.doFilter(request, response);
            return;

        }

        state.setValue(UUID.randomUUID().toString());
        ClientIdentifier clientIdentifier = new ClientIdentifier(clientId, clientSecret);
        OAuth2CodeGrantFlow flow = OAuth2ClientSupport.authorizationCodeGrantFlowBuilder(
                clientIdentifier, authorizationUri, accessTokenUri)
                .redirectUri(redirectUri)
                .client(client)
                .property(Phase.ALL, OAuth2Parameters.STATE, state.getValue())
                .build();
        String location = flow.start();
        ((HttpServletResponse) response).sendRedirect(location);
    }

    @Override
    public void destroy() {
    }
}
