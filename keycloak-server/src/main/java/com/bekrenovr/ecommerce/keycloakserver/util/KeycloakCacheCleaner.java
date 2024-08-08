package com.bekrenovr.ecommerce.keycloakserver.util;

import com.bekrenovr.ecommerce.keycloakserver.config.properties.ApplicationPropertiesHolder;
import com.bekrenovr.ecommerce.keycloakserver.config.properties.KeycloakServerProperties;
import org.apache.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class KeycloakCacheCleaner {
    private static final String CLEAN_USERS_CACHE_REQUEST_PATH = "/admin/realms/master/clear-user-cache";
    private final InternalAuthenticator internalAuthenticator;

    public KeycloakCacheCleaner() {
        internalAuthenticator = new InternalAuthenticator();
    }

    public void cleanUsersCache() {
        KeycloakServerProperties serverProperties = ApplicationPropertiesHolder.getKeycloakServerProperties();
        RestTemplate restTemplate = new RestTemplate();
        String adminAccessToken = internalAuthenticator.authenticateAsAdmin();
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(serverProperties.getContextPath() + CLEAN_USERS_CACHE_REQUEST_PATH)
                .build()
                .toUri();
        RequestEntity<Void> request = RequestEntity.post(uri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAccessToken)
                .build();
        restTemplate.exchange(request, Void.class);
    }
}
