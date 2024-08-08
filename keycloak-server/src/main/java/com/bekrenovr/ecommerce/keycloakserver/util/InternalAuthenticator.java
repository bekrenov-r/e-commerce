package com.bekrenovr.ecommerce.keycloakserver.util;

import com.bekrenovr.ecommerce.keycloakserver.config.properties.ApplicationPropertiesHolder;
import com.bekrenovr.ecommerce.keycloakserver.config.properties.KeycloakServerProperties;
import com.jayway.jsonpath.JsonPath;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class InternalAuthenticator {
    private static final String ADMIN_AUTHENTICATION_PATH = "/realms/master/protocol/openid-connect/token";

    public String authenticateAsAdmin() {
        KeycloakServerProperties serverProperties = ApplicationPropertiesHolder.getKeycloakServerProperties();
        RestTemplate restTemplate = new RestTemplate();
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(serverProperties.getContextPath() + ADMIN_AUTHENTICATION_PATH)
                .build()
                .toUri();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("username", serverProperties.getAdminUser().getUsername());
        body.add("password", serverProperties.getAdminUser().getPassword());
        body.add("client_id", serverProperties.getAdminClient().getClientId());
        body.add("client_secret", serverProperties.getAdminClient().getClientSecret());
        RequestEntity<MultiValueMap<String, String>> request = RequestEntity.post(uri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED)
                .body(body);
        ResponseEntity<Object> response = restTemplate.exchange(request, Object.class);
        return JsonPath.read(response.getBody(), "$.access_token");
    }
}
