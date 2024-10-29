package com.bekrenovr.ecommerce.customers.integration;

import com.bekrenovr.ecommerce.customers.util.CustomerJsonBuilder;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpHeaders;
import org.json.JSONException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class CustomerIT {
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = IntegrationTestsPostgreSQLContainer.getInstance();

    @Autowired
    TestRestTemplate restTemplate;

    static final String URI_MAPPING = "/customers";

    @Nested
    class GetByEmail {
        @Test
        void shouldReturn200_WhenCustomerExists() {
            String email = "jane.doe@example.com";
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("email", email)
                    .build().toUri();

            ResponseEntity<?> response =  restTemplate.getForEntity(uri, Object.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturn404_WhenCustomerDoesntExist() {
            String email = "does.not.exist@example.com";
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("email", email)
                    .build().toUri();

            ResponseEntity<?> response =  restTemplate.getForEntity(uri, Object.class);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    class Create {
        @Test
        void shouldReturn201_whenCustomerCreatedSuccessfully() throws JSONException {
            String json = CustomerJsonBuilder.create()
                    .firstName("John")
                    .lastName("Doe")
                    .email("new.customer@example.com")
                    .build().toString();
            RequestEntity<String> request = RequestEntity.post(URI.create(URI_MAPPING))
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .body(json);

            ResponseEntity<?> response = restTemplate.exchange(request, Object.class);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }

        @Test
        void shouldReturn200_whenCustomerExistsAndIsNotRegistered() throws JSONException {
            String json = CustomerJsonBuilder.create()
                    .firstName("Alex")
                    .lastName("Mitchell")
                    .email("alex.mitchell@example.com")
                    .build().toString();
            RequestEntity<String> request = RequestEntity.post(URI.create(URI_MAPPING))
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .body(json);

            ResponseEntity<?> response = restTemplate.exchange(request, Object.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturn409_whenCustomerExistsAndIsRegistered() throws JSONException {
            String json = CustomerJsonBuilder.create()
                    .firstName("Jane")
                    .lastName("Doe")
                    .email("jane.doe@example.com")
                    .build().toString();
            RequestEntity<String> request = RequestEntity.post(URI.create(URI_MAPPING))
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .body(json);

            ResponseEntity<?> response = restTemplate.exchange(request, Object.class);

            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        }
    }

    @Nested
    class Update {
        @Test
        void shouldReturn200_whenCustomerUpdatedSuccessfully() throws JSONException {
            String json = CustomerJsonBuilder.create()
                    .firstName("new name")
                    .lastName("new surname")
                    .email("jane.doe@example.com")
                    .build().toString();
            RequestEntity<String> request = RequestEntity.put(URI.create(URI_MAPPING))
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .body(json);

            ResponseEntity<?> response = restTemplate.exchange(request, Object.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturn404_WhenCustomerDoesNotExist() throws JSONException {
            String json = CustomerJsonBuilder.create()
                    .firstName("John")
                    .lastName("Doe")
                    .email("does.not.exist@example.com")
                    .build().toString();
            RequestEntity<String> request = RequestEntity.put(URI.create(URI_MAPPING))
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .body(json);

            ResponseEntity<?> response = restTemplate.exchange(request, Object.class);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }
}
