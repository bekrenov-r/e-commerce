package com.ecommerce.bekrenovr.authorizationserver.integration;

import com.bekrenovr.ecommerce.common.model.Person;
import com.ecommerce.bekrenovr.authorizationserver.dto.response.CustomerResponse;
import com.ecommerce.bekrenovr.authorizationserver.proxy.CustomerServiceProxy;
import com.ecommerce.bekrenovr.authorizationserver.proxy.KeycloakProxy;
import feign.FeignException;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserIT extends BaseIT {
    static final String URI_MAPPING = "/users/recover-password";

    @MockBean
    CustomerServiceProxy customerServiceProxy;
    @MockBean
    KeycloakProxy keycloakProxy;

    @Autowired
    UserIT(TestRestTemplate restTemplate) {
        super(restTemplate);
    }

    @Nested
    class SendEmailForPasswordRecovery {
        @Test
        void shouldReturn200_WhenEmailSentSuccessfully() {
            CustomerResponse mockCustomer = mockCustomer();
            when(customerServiceProxy.getCustomerByEmail(anyString())).thenReturn(ResponseEntity.ok(mockCustomer));
            when(keycloakProxy.createPasswordRecoveryToken(anyString(), anyString()))
                    .thenReturn(ResponseEntity.ok().build());
            doNothing().when(mailService).sendPasswordRecoveryEmail(any(Person.class), anyString());

            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("email", "test.user@example.com")
                    .build().toUri();
            RequestEntity<Void> request = RequestEntity.post(uri).build();

            ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturn200_WhenUserWasNotRegisteredInKeycloak() {
            CustomerResponse mockCustomer = mockCustomer();
            when(customerServiceProxy.getCustomerByEmail(anyString())).thenReturn(ResponseEntity.ok(mockCustomer));
            FeignException.NotFound notFound = mock(FeignException.NotFound.class);
            when(notFound.status()).thenReturn(404);
            when(keycloakProxy.createPasswordRecoveryToken(anyString(), anyString()))
                    .thenThrow(notFound)
                    .thenReturn(ResponseEntity.ok().build());
            when(keycloakProxy.createKeycloakUser(anyString(), anyString(), anyString(), nullable(String.class)))
                    .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body("token"));
            when(keycloakProxy.enableUser(anyString())).thenReturn(ResponseEntity.ok().build());
            doNothing().when(mailService).sendPasswordRecoveryEmail(any(Person.class), anyString());

            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("email", "test.user@example.com")
                    .build().toUri();
            RequestEntity<Void> request = RequestEntity.post(uri).build();

            ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturn404_WhenCustomerDoesNotExist() {
            FeignException notFound = mockFeignException(HttpStatus.NOT_FOUND);
            when(customerServiceProxy.getCustomerByEmail(anyString())).thenThrow(notFound);

            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("email", "test.user@example.com")
                    .build().toUri();
            RequestEntity<Void> request = RequestEntity.post(uri).build();

            ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        void shouldReturn403_WhenUserDisabled() {
            CustomerResponse mockCustomer = mockCustomer();
            when(customerServiceProxy.getCustomerByEmail(anyString())).thenReturn(ResponseEntity.ok(mockCustomer));
            FeignException forbidden = mockFeignException(HttpStatus.FORBIDDEN);
            when(keycloakProxy.createPasswordRecoveryToken(anyString(), anyString())).thenThrow(forbidden);

            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("email", "test.user@example.com")
                    .build().toUri();
            RequestEntity<Void> request = RequestEntity.post(uri).build();

            ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }
    }

    @Nested
    class RecoverPassword {
        @Test
        void shouldReturn200_WhenPasswordRecoveredSuccessfully() {
            CustomerResponse mockCustomer = mockCustomer();
            when(customerServiceProxy.getCustomerByEmail(anyString())).thenReturn(ResponseEntity.ok(mockCustomer));
            FeignException forbidden = mockFeignException(HttpStatus.FORBIDDEN);
            when(keycloakProxy.createPasswordRecoveryToken(anyString(), anyString())).thenThrow(forbidden);

            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("email", "test.user@example.com")
                    .build().toUri();
            RequestEntity<Void> request = RequestEntity.post(uri).build();

            ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }

        @Test
        void shouldReturn404_WhenPasswordRecoveryTokenDoesNotExist() {

        }
    }

    private static CustomerResponse mockCustomer() {
        CustomerResponse p = mock(CustomerResponse.class);
        when(p.getEmail()).thenReturn("test.user@example.com");
        return p;
    }

    private static FeignException mockFeignException(HttpStatus status) {
        FeignException ex = mock(FeignException.class);
        when(ex.status()).thenReturn(status.value());
        return ex;
    }
}
