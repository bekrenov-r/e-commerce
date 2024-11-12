package com.ecommerce.bekrenovr.authorizationserver.integration;

import com.ecommerce.bekrenovr.authorizationserver.dto.request.CustomerRequest;
import com.ecommerce.bekrenovr.authorizationserver.proxy.CustomerServiceProxy;
import com.ecommerce.bekrenovr.authorizationserver.util.GoogleApiClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class GoogleLoginIT {
    static final String URI_MAPPING = "/login/google";
    TestRestTemplate restTemplate;

    @MockBean
    CustomerServiceProxy customerServiceProxy;
    @MockBean
    GoogleApiClient googleApiClient;

    @Autowired
    GoogleLoginIT(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Nested
    class GrantTypeAuthorizationCode {
        @Test
        void shouldReturn200_WhenAccessTokenGranted() throws JSONException {
            JSONObject userInfoJSON = new JSONObject()
                    .put("given_name", "Test")
                    .put("family_name", "User")
                    .put("email", "test.user@example.com");
            when(googleApiClient.getAccessTokenResponse(anyString()))
                    .thenReturn(new JSONObject().put("access_token", ""));
            when(googleApiClient.getGoogleUserInfo(anyString())).thenReturn(userInfoJSON);
            when(customerServiceProxy.createCustomer(any(CustomerRequest.class)))
                    .thenReturn(ResponseEntity.ok().build());

            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("grant_type", "authorization_code")
                    .queryParam("code", "4/0AQlEd8xBvvFVJWUGf7HRnQO4K3wnE5oElIxvTBGfKuf734KZURSVmLevpekCtrLUqL49LQ")
                    .build().toUri();
            RequestEntity<Void> request = RequestEntity.post(uri).build();

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturn401_WhenAuthorizationCodeInvalid() {
            HttpClientErrorException unauthorized = mockUnauthorizedException();
            when(googleApiClient.getAccessTokenResponse(anyString())).thenThrow(unauthorized);

            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("grant_type", "authorization_code")
                    .queryParam("code", "")
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
            when(googleApiClient.refreshAccessToken(anyString()))
                    .thenReturn("");

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
            HttpClientErrorException unauthorized = mockUnauthorizedException();
            when(googleApiClient.refreshAccessToken(anyString())).thenThrow(unauthorized);

            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("grant_type", "refresh_token")
                    .queryParam("refresh_token", "")
                    .build().toUri();
            RequestEntity<Void> request = RequestEntity.post(uri).build();

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);

            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        }
    }

    private static HttpClientErrorException mockUnauthorizedException() {
        HttpClientErrorException unauthorized = mock(HttpClientErrorException.class);
        when(unauthorized.getStatusCode()).thenReturn(HttpStatus.UNAUTHORIZED);
        return unauthorized;
    }
}
