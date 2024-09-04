package com.bekrenovr.ecommerce.users.service;

import com.bekrenovr.ecommerce.users.config.OAuth2LoginProviderProperties;
import com.bekrenovr.ecommerce.users.config.OAuth2LoginProviderProperties.Providers;
import com.bekrenovr.ecommerce.users.proxy.KeycloakProxy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;

@Service
public class KeycloakLoginService {
    private final KeycloakProxy keycloakProxy;
    private final OAuth2LoginProviderProperties.LoginProvider keycloakProperties;

    public KeycloakLoginService(KeycloakProxy keycloakProxy, OAuth2LoginProviderProperties loginProviderProperties) {
        this.keycloakProxy = keycloakProxy;
        this.keycloakProperties = loginProviderProperties.get(Providers.KEYCLOAK.name());
    }

    public ResponseEntity<String> getAccessTokenBasic(String username, String password) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("username", username);
        form.add("password", password);
        form.add("client_id", keycloakProperties.getClientId());
        form.add("client_secret", keycloakProperties.getClientSecret());
        Arrays.asList(keycloakProperties.getScopes()).forEach(s -> form.add("scope", s));
        form.add("grant_type", keycloakProperties.getGrantType());
        return keycloakProxy.getAccessToken(form);
    }

    public ResponseEntity<String> refreshAccessTokenBasic(String refreshToken) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("refresh_token", refreshToken);
        form.add("client_id", keycloakProperties.getClientId());
        form.add("client_secret", keycloakProperties.getClientSecret());
        form.add("grant_type", "refresh_token");
        return keycloakProxy.getAccessToken(form);
    }
}
