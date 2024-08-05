package com.bekrenovr.ecommerce.keycloakserver.util;

import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class KeycloakCacheCleaner {
    private static final String CLEAN_USERS_CACHE_REQUEST_PATH = "/embedded/admin/realms/master/clear-user-cache";
    private final InternalAuthenticator internalAuthenticator;
    public void cleanUsersCache() {
        RestTemplate restTemplate = new RestTemplate();
        String adminAccessToken = internalAuthenticator.authenticateAsAdmin();
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(CLEAN_USERS_CACHE_REQUEST_PATH)
                .build()
                .toUri();
        RequestEntity<Void> request = RequestEntity.post(uri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAccessToken)
                .build();
        restTemplate.exchange(request, Void.class);
    }
}
