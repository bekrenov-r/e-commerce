package com.ecommerce.bekrenovr.authorizationserver.integration;

import com.bekrenovr.ecommerce.common.model.Person;
import com.ecommerce.bekrenovr.authorizationserver.dto.request.CustomerRegistrationRequest;
import com.ecommerce.bekrenovr.authorizationserver.proxy.CustomerServiceProxy;
import com.ecommerce.bekrenovr.authorizationserver.proxy.KeycloakProxy;
import com.ecommerce.bekrenovr.authorizationserver.service.MailService;
import com.ecommerce.bekrenovr.authorizationserver.util.CustomerRegistrationRequestJsonBuilder;
import feign.FeignException;
import jakarta.ws.rs.core.MediaType;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpHeaders;
import org.json.JSONException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class RegistrationIT {
    TestRestTemplate restTemplate;

    @MockBean
    MailService mailService;
    @MockBean
    KeycloakProxy keycloakProxy;
    @MockBean
    CustomerServiceProxy customerServiceProxy;

    @Autowired
    RegistrationIT(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Nested
    class RegisterCustomer {
        static final String URI_MAPPING = "/registration/customer";
        @Test
        void shouldReturn201_WhenCustomerRegisteredSuccessfully() throws JSONException {
            doNothing().when(mailService).sendCustomerAccountActivationEmail(any(Person.class), anyString());
            when(keycloakProxy.createKeycloakUser(anyString(), anyString(), anyString(), anyString()))
                    .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(""));
            when(customerServiceProxy.createCustomer(any(CustomerRegistrationRequest.class)))
                    .thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());

            String requestBody = CustomerRegistrationRequestJsonBuilder.create()
                    .firstName("John")
                    .lastName("Doe")
                    .email("test.customer@example.com")
                    .password("password")
                    .build().toString();
            RequestEntity<String> request = RequestEntity.post(URI_MAPPING)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .body(requestBody);

            ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }

        @Test
        void shouldReturn409_WhenKeycloakUserAlreadyExists() throws JSONException {
            FeignException conflict = mockFeignException(HttpStatus.CONFLICT);
            when(keycloakProxy.createKeycloakUser(anyString(), anyString(), anyString(), anyString()))
                    .thenThrow(conflict);

            String requestBody = CustomerRegistrationRequestJsonBuilder.create()
                    .firstName("John")
                    .lastName("Doe")
                    .email("test.customer@example.com")
                    .password("password")
                    .build().toString();
            RequestEntity<String> request = RequestEntity.post(URI_MAPPING)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .body(requestBody);

            ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        }

        @Test
        void shouldReturn409_WhenCustomerAlreadyExists() throws JSONException {
            when(keycloakProxy.createKeycloakUser(anyString(), anyString(), anyString(), anyString()))
                    .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(""));
            FeignException conflict = mockFeignException(HttpStatus.CONFLICT);
            when(customerServiceProxy.createCustomer(any(CustomerRegistrationRequest.class))).thenThrow(conflict);

            String requestBody = CustomerRegistrationRequestJsonBuilder.create()
                    .firstName("John")
                    .lastName("Doe")
                    .email("test.customer@example.com")
                    .password("password")
                    .build().toString();
            RequestEntity<String> request = RequestEntity.post(URI_MAPPING)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .body(requestBody);

            ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        }
    }

    @Nested
    class ResendActivationEmail {
        static final String URI_MAPPING = "/registration/customer/resend-email";

        @Test
        void shouldReturn200_WhenResendSuccessful() {
            ResponseEntity<String> mockKeycloakResponse = mock(ResponseEntity.class);
            when(mockKeycloakResponse.getBody()).thenReturn("");
            doNothing().when(mailService).sendCustomerAccountActivationEmail(any(Person.class), anyString());
            when(customerServiceProxy.getCustomerByEmail(anyString())).thenReturn(ResponseEntity.ok().build());
            when(keycloakProxy.getActivationTokenForUser(anyString())).thenReturn(mockKeycloakResponse);

            String email = "test.customer@example.com";
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("email", email)
                    .build().toUri();
            RequestEntity<Void> request = RequestEntity.post(uri).build();

            ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturn404_WhenCustomerWithEmailDoesNotExist() {
            FeignException notFound = mockFeignException(HttpStatus.NOT_FOUND);
            when(customerServiceProxy.getCustomerByEmail(anyString()))
                    .thenThrow(notFound);

            String email = "test.customer@example.com";
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("email", email)
                    .build().toUri();
            RequestEntity<Void> request = RequestEntity.post(uri).build();

            ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        void shouldReturn404_WhenActivationTokenNotFound() {
            when(customerServiceProxy.getCustomerByEmail(anyString())).thenReturn(ResponseEntity.ok().build());
            FeignException notFound = mockFeignException(HttpStatus.NOT_FOUND);
            when(keycloakProxy.getActivationTokenForUser(anyString()))
                    .thenThrow(notFound);

            String email = "test.customer@example.com";
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("email", email)
                    .build().toUri();
            RequestEntity<Void> request = RequestEntity.post(uri).build();

            ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    class ActivateAccount {
        static final String URI_MAPPING = "/registration/customer/activate-account";

        @Test
        void shouldReturn200_WhenUserActivatedSuccessfully() {
            doNothing().when(mailService).sendCustomerAccountActivationEmail(any(Person.class), anyString());
            when(keycloakProxy.enableUser(anyString()))
                    .thenReturn(ResponseEntity.ok().build());

            String token = RandomStringUtils.random(10, true, true);
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("token", token)
                    .build().toUri();
            RequestEntity<Void> request = RequestEntity.post(uri).build();

            ResponseEntity<?> response = restTemplate.exchange(request, Object.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturn404_WhenActivationTokenNotFound() {
            FeignException notFound = mockFeignException(HttpStatus.NOT_FOUND);
            when(keycloakProxy.enableUser(anyString())).thenThrow(notFound);

            String token = RandomStringUtils.random(20, true, true);
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("token", token)
                    .build().toUri();
            RequestEntity<Void> request = RequestEntity.post(uri).build();

            ResponseEntity<?> response = restTemplate.exchange(request, Object.class);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    private static FeignException mockFeignException(HttpStatus status) {
        FeignException ex = Mockito.mock(FeignException.class);
        when(ex.status()).thenReturn(status.value());
        return ex;
    }
}
