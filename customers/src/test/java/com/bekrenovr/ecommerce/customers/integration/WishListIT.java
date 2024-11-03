package com.bekrenovr.ecommerce.customers.integration;

import com.bekrenovr.ecommerce.common.security.Role;
import com.bekrenovr.ecommerce.common.security.SecurityConstants;
import com.bekrenovr.ecommerce.common.util.TestUtil;
import com.bekrenovr.ecommerce.customers.proxy.CatalogProxy;
import feign.FeignException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class WishListIT {
    static final String URI_MAPPING = "/customers/wishlist";
    TestRestTemplate restTemplate;
    @MockBean
    CatalogProxy catalogProxy;

    @Autowired
    WishListIT(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Nested
    class GetForCustomer {
        @Test
        void shouldReturn200_WhenWishListHasItems() {
            String authenticatedUser =  TestUtil.getAuthenticatedUserJSON(
                    "jane.doe@example.com", List.of(Role.CUSTOMER));
            RequestEntity<Void> request = RequestEntity.get(URI.create(URI_MAPPING))
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedUser)
                    .build();

            ResponseEntity<?> response = restTemplate.exchange(request, Object.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturn200_WhenWishListIsEmpty() {
            String authenticatedUser =  TestUtil.getAuthenticatedUserJSON(
                    "roman.bekrenov@interia.pl", List.of(Role.CUSTOMER));
            RequestEntity<Void> request = RequestEntity.get(URI.create(URI_MAPPING))
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedUser)
                    .build();

            ResponseEntity<?> response = restTemplate.exchange(request, Object.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Nested
    class AddItem {
        @Test
        void shouldReturn201_WhenItemAddedSuccessfully() {
            when(catalogProxy.getItemById(any(UUID.class))).thenReturn(ResponseEntity.ok().build());

            String authenticatedUser = TestUtil.getAuthenticatedUserJSON(
                    "jane.doe@example.com", List.of(Role.CUSTOMER));
            String itemId = UUID.randomUUID().toString();
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("id", itemId)
                    .build().toUri();
            RequestEntity<Void> request = RequestEntity.post(uri)
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedUser)
                    .build();

            ResponseEntity<?> response = restTemplate.exchange(request, Object.class);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }

        @Test
        void shouldReturn404_WhenItemDoesNotExist() {
            when(catalogProxy.getItemById(any(UUID.class))).thenThrow(mockFeignException(HttpStatus.NOT_FOUND));

            String authenticatedUser = TestUtil.getAuthenticatedUserJSON(
                    "jane.doe@example.com", List.of(Role.CUSTOMER));
            String itemId = UUID.randomUUID().toString();
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("id", itemId)
                    .build().toUri();
            RequestEntity<Void> request = RequestEntity.post(uri)
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedUser)
                    .build();

            ResponseEntity<?> response = restTemplate.exchange(request, Object.class);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        void shouldReturn404_WhenAuthenticatedCustomerDoesNotExist() {
            when(catalogProxy.getItemById(any(UUID.class))).thenReturn(ResponseEntity.ok().build());

            String authenticatedUser = TestUtil.getAuthenticatedUserJSON(
                    "does.not.exist@example.com", List.of(Role.CUSTOMER));
            String itemId = UUID.randomUUID().toString();
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("id", itemId)
                    .build().toUri();
            RequestEntity<Void> request = RequestEntity.post(uri)
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedUser)
                    .build();

            ResponseEntity<?> response = restTemplate.exchange(request, Object.class);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        void shouldReturn409_WhenItemIsAlreadyOnWishList() {
            when(catalogProxy.getItemById(any(UUID.class))).thenReturn(ResponseEntity.ok().build());

            String authenticatedUser = TestUtil.getAuthenticatedUserJSON(
                    "jane.doe@example.com", List.of(Role.CUSTOMER));
            String itemId = "b867560e-a92f-44a3-8096-a0f088034460";
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("id", itemId)
                    .build().toUri();
            RequestEntity<Void> request = RequestEntity.post(uri)
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedUser)
                    .build();

            ResponseEntity<?> response = restTemplate.exchange(request, Object.class);

            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        }

        private static FeignException mockFeignException(HttpStatus status) {
            FeignException ex = Mockito.mock(FeignException.class);
            when(ex.status()).thenReturn(status.value());
            return ex;
        }
    }

    @Nested
    class RemoveItem {
        @Test
        void shouldReturn200_WhenItemRemovedSuccessfully() {
            String authenticatedUser = TestUtil.getAuthenticatedUserJSON(
                    "jane.doe@example.com", List.of(Role.CUSTOMER));
            String itemId = "fa5f648e-b2b8-4f8a-a0d7-d65a4547f8d6";
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("id", itemId)
                    .build().toUri();
            RequestEntity<Void> request = RequestEntity.delete(uri)
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedUser)
                    .build();

            ResponseEntity<?> response = restTemplate.exchange(request, Object.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturn404_WhenItemIsNotOnWishList() {
            String authenticatedUser = TestUtil.getAuthenticatedUserJSON(
                    "jane.doe@example.com", List.of(Role.CUSTOMER));
            String itemId = UUID.randomUUID().toString();
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("id", itemId)
                    .build().toUri();
            RequestEntity<Void> request = RequestEntity.delete(uri)
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedUser)
                    .build();

            ResponseEntity<?> response = restTemplate.exchange(request, Object.class);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        void shouldReturn404_WhenAuthenticatedCustomerDoesNotExist() {
            String authenticatedUser = TestUtil.getAuthenticatedUserJSON(
                    "does.not.exist@example.com", List.of(Role.CUSTOMER));
            String itemId = UUID.randomUUID().toString();
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("id", itemId)
                    .build().toUri();
            RequestEntity<Void> request = RequestEntity.delete(uri)
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedUser)
                    .build();

            ResponseEntity<?> response = restTemplate.exchange(request, Object.class);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    class Clear {
        @Test
        void shouldReturn200_WhenCartClearedSuccessfully() {
            String authenticatedUser = TestUtil.getAuthenticatedUserJSON(
                    "bekrenov.s@gmail.com", List.of(Role.CUSTOMER));
            RequestEntity<Void> request = RequestEntity.delete(URI.create(URI_MAPPING + "/clear"))
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedUser)
                    .build();

            ResponseEntity<?> response = restTemplate.exchange(request, Object.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturn404_WhenAuthenticatedCustomerDoesNotExist() {
            String authenticatedUser = TestUtil.getAuthenticatedUserJSON(
                    "does.not.exist@example.com", List.of(Role.CUSTOMER));
            RequestEntity<Void> request = RequestEntity.delete(URI.create(URI_MAPPING + "/clear"))
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedUser)
                    .build();

            ResponseEntity<?> response = restTemplate.exchange(request, Object.class);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }
}
