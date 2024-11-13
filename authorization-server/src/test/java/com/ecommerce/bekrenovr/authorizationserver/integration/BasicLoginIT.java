package com.ecommerce.bekrenovr.authorizationserver.integration;

import com.ecommerce.bekrenovr.authorizationserver.feign.KeycloakProxy;
import feign.FeignException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BasicLoginIT extends BaseIT {
    static final String URI_MAPPING = "/login/basic";

    @MockBean
    KeycloakProxy keycloakProxy;

    @Autowired
    BasicLoginIT(TestRestTemplate restTemplate) {
        super(restTemplate);
    }

    @Nested
    class GrantTypePassword {
        @Test
        void shouldReturn200_WhenAccessTokenGranted() {
            when(keycloakProxy.getAccessToken(anyMap())).thenReturn(ResponseEntity.ok().build());

            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("grant_type", "password")
                    .queryParam("username", "test.user@example.com")
                    .queryParam("password", "password")
                    .build().toUri();
            RequestEntity<Void> request = RequestEntity.post(uri).build();

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturn401_WhenCredentialsInvalid() {
            FeignException unauthorized = mockFeignException(HttpStatus.UNAUTHORIZED);
            when(keycloakProxy.getAccessToken(anyMap())).thenThrow(unauthorized);

            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("grant_type", "password")
                    .queryParam("username", "test.user@example.com")
                    .queryParam("password", "password")
                    .build().toUri();
            RequestEntity<Void> request = RequestEntity.post(uri).build();

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);

            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        }
    }

    @Nested
    class GrantTypeRefreshToken {
        @Test
        void shouldReturn200_WhenAccessTokenGranted() {
            when(keycloakProxy.getAccessToken(anyMap())).thenReturn(ResponseEntity.ok().build());

            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("grant_type", "refresh_token")
                    .queryParam("refresh_token", "")
                    .build().toUri();
            RequestEntity<Void> request = RequestEntity.post(uri).build();

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturn401_WhenRefreshTokenInvalid() {
            FeignException unauthorized = mockFeignException(HttpStatus.UNAUTHORIZED);
            when(keycloakProxy.getAccessToken(anyMap())).thenThrow(unauthorized);

            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("grant_type", "refresh_token")
                    .queryParam("refresh_token", "")
                    .build().toUri();
            RequestEntity<Void> request = RequestEntity.post(uri).build();

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);

            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        }
    }

    private static FeignException mockFeignException(HttpStatus status) {
        FeignException ex = Mockito.mock(FeignException.class);
        when(ex.status()).thenReturn(status.value());
        return ex;
    }
}
