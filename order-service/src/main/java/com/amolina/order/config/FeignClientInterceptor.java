package com.amolina.order.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN_TYPE = "Bearer";

    @Override
    public void apply(RequestTemplate template) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication instanceof KeycloakAuthenticationToken) {
            KeycloakAuthenticationToken keycloakToken = (KeycloakAuthenticationToken) authentication;
            KeycloakSecurityContext securityContext = keycloakToken.getAccount().getKeycloakSecurityContext();
            String accessToken = securityContext.getTokenString();
            
            if (accessToken != null) {
                template.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE, accessToken));
            }
        }
    }
}

