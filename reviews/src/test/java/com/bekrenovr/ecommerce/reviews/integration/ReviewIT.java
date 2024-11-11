package com.bekrenovr.ecommerce.reviews.integration;

import com.bekrenovr.ecommerce.common.security.Role;
import com.bekrenovr.ecommerce.common.security.SecurityConstants;
import com.bekrenovr.ecommerce.common.util.TestUtil;
import com.bekrenovr.ecommerce.reviews.feign.CatalogProxy;
import com.bekrenovr.ecommerce.reviews.feign.CustomersProxy;
import com.bekrenovr.ecommerce.reviews.feign.OrdersProxy;
import com.bekrenovr.ecommerce.reviews.review.Review;
import com.bekrenovr.ecommerce.reviews.review.ReviewRepository;
import com.bekrenovr.ecommerce.reviews.util.ReviewRequestJsonBuilder;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import feign.FeignException;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.URI;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class ReviewIT {
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");
    static final String URI_MAPPING = "/reviews";

    TestRestTemplate restTemplate;

    @MockBean
    CatalogProxy catalogProxy;
    @MockBean
    CustomersProxy customersProxy;
    @MockBean
    OrdersProxy ordersProxy;
    @SpyBean
    ReviewRepository reviewRepository;

    @Autowired
    ReviewIT(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @DynamicPropertySource
    static void containersProperties(DynamicPropertyRegistry registry) {
        mongoDBContainer.start();
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Nested
    class GetAllForItem {
        @Test
        void shouldReturn200_ForExistingItem() {
            when(catalogProxy.getItemById(any(UUID.class))).thenReturn(ResponseEntity.ok().build());
            mockCustomersProxy();
            String itemId = "ea7cb002-3fea-4051-9f8d-c86ca4d4a696";
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("itemId", itemId)
                    .build().toUri();
            RequestEntity<Void> request = RequestEntity.get(uri).build();

            ResponseEntity<?> response = restTemplate.exchange(request, Object.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturn200AndEmptyPage_ForExistingItemWithNoReviews() {
            when(catalogProxy.getItemById(any(UUID.class))).thenReturn(ResponseEntity.ok().build());
            mockCustomersProxy();
            String itemId = UUID.randomUUID().toString();
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("itemId", itemId)
                    .build().toUri();
            RequestEntity<Void> request = RequestEntity.get(uri).build();

            ResponseEntity<?> response = restTemplate.exchange(request, Object.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());

            DocumentContext json = JsonPath.parse(response.getBody());
            assertTrue(json.read("$.content", List.class).isEmpty());
            assertEquals(0, json.read("$.totalElements", Integer.class));
            assertEquals(0, json.read("$.totalPages", Integer.class));
            assertTrue(json.read("$.empty", Boolean.class));

        }

        @Test
        void shouldReturn404_ForNonExistentItem() {
            FeignException notFound = mockFeignException(HttpStatus.NOT_FOUND);
            when(catalogProxy.getItemById(any(UUID.class))).thenThrow(notFound);
            mockCustomersProxy();
            String itemId = UUID.randomUUID().toString();
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING)
                    .queryParam("itemId", itemId)
                    .build().toUri();
            RequestEntity<Void> request = RequestEntity.get(uri).build();

            ResponseEntity<?> response = restTemplate.exchange(request, Object.class);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    class Create {
        @Test
        void shouldReturn201_WhenReviewCreatedSuccessfully() {
            UUID itemId = UUID.randomUUID();
            Map<?, ?> order = Map.of("itemEntries", List.of(Map.of("itemId", itemId.toString())));
            List<Map<?, ?>> mockOrders = List.of(new LinkedHashMap<>(order));
            ResponseEntity<Page<Map<?, ?>>> mockResponse = ResponseEntity.ok(new PageImpl<>(mockOrders));
            when(ordersProxy.getAllOrdersForCustomer(anyString(), anyString(), anyInt(), anyInt()))
                    .thenReturn(mockResponse);
            String json = ReviewRequestJsonBuilder.create()
                    .itemId(itemId)
                    .title("Good stuff")
                    .content("Very good item")
                    .rating(5)
                    .build().toString(4);
            String authenticatedUser = TestUtil.getAuthenticatedUserJSON(
                    "bekrenov.s@gmail.com", Set.of(Role.CUSTOMER));
            RequestEntity<String> request = RequestEntity.post(URI_MAPPING)
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedUser)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .body(json);

            ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }

        @Test
        void shouldReturn409_WhenReviewAlreadyExists() {
            when(reviewRepository.existsByItemIdAndCustomerEmail(any(UUID.class), anyString()))
                    .thenReturn(true);
            String json = ReviewRequestJsonBuilder.create()
                    .itemId(UUID.randomUUID())
                    .title("Good stuff")
                    .content("Very good item")
                    .rating(5)
                    .build().toString(4);
            String authenticatedUser = TestUtil.getAuthenticatedUserJSON(
                    "bekrenov.s@gmail.com", Set.of(Role.CUSTOMER));
            RequestEntity<String> request = RequestEntity.post(URI_MAPPING)
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedUser)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .body(json);

            ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        }

        @Test
        void shouldReturn400_WhenCustomerHasNoOrders() {
            ResponseEntity<Page<Map<?, ?>>> mockResponse = ResponseEntity.ok(Page.empty());
            when(ordersProxy.getAllOrdersForCustomer(anyString(), anyString(), anyInt(), anyInt()))
                    .thenReturn(mockResponse);
            String json = ReviewRequestJsonBuilder.create()
                    .itemId(UUID.randomUUID())
                    .title("Good stuff")
                    .content("Very good item")
                    .rating(5)
                    .build().toString(4);
            String authenticatedUser = TestUtil.getAuthenticatedUserJSON(
                    "bekrenov.s@gmail.com", Set.of(Role.CUSTOMER));
            RequestEntity<String> request = RequestEntity.post(URI_MAPPING)
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedUser)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .body(json);

            ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

        @Test
        void shouldReturn400_WhenCustomerHasNoCompletedOrderForItem() {
            Map<?, ?> order = Map.of("itemEntries", List.of(Map.of("itemId", UUID.randomUUID())));
            List<Map<?, ?>> mockOrders = List.of(new LinkedHashMap<>(order));
            ResponseEntity<Page<Map<?, ?>>> mockResponse = ResponseEntity.ok(new PageImpl<>(mockOrders));
            when(ordersProxy.getAllOrdersForCustomer(anyString(), anyString(), anyInt(), anyInt()))
                    .thenReturn(mockResponse);
            String json = ReviewRequestJsonBuilder.create()
                    .itemId(UUID.randomUUID())
                    .title("Good stuff")
                    .content("Very good item")
                    .rating(5)
                    .build().toString(4);
            String authenticatedUser = TestUtil.getAuthenticatedUserJSON(
                    "bekrenov.s@gmail.com", Set.of(Role.CUSTOMER));
            RequestEntity<String> request = RequestEntity.post(URI_MAPPING)
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedUser)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .body(json);

            ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Nested
    class Update {
        @Test
        void shouldReturn200_WhenUpdateSuccessful() {
            mockCustomersProxy();
            String customerEmail = "bekrenov.s@gmail.com";
            Review review = findAnyReviewForCustomer(customerEmail);
            String json = ReviewRequestJsonBuilder.create()
                    .itemId(review.getItemId())
                    .title("New Title")
                    .content("Another content")
                    .rating(3)
                    .build().toString(4);
            String authenticatedUser = TestUtil.getAuthenticatedUserJSON(customerEmail, Set.of(Role.CUSTOMER));
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING + "/{id}").build(review.getId());
            RequestEntity<String> request = RequestEntity.put(uri)
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedUser)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .body(json);

            ResponseEntity<?> response = restTemplate.exchange(request, Object.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturn404_WhenReviewNotFound() {
            String reviewId = "does_not_exist";
            String json = ReviewRequestJsonBuilder.create()
                    .itemId(UUID.randomUUID())
                    .title("New Title")
                    .content("Another content")
                    .rating(3)
                    .build().toString(4);
            String authenticatedUser = TestUtil.getAuthenticatedUserJSON(
                    "bekrenov.s@gmail.com", Set.of(Role.CUSTOMER));
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING + "/{id}").build(reviewId);
            RequestEntity<String> request = RequestEntity.put(uri)
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedUser)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .body(json);

            ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        void shouldReturn403_WhenCustomerIsNotReviewOwner() {
            String reviewId = findAnyReviewForCustomer("bekrenov.s@gmail.com").getId();
            String json = ReviewRequestJsonBuilder.create()
                    .itemId(UUID.randomUUID())
                    .title("New Title")
                    .content("Another content")
                    .rating(3)
                    .build().toString(4);
            String authenticatedUser = TestUtil.getAuthenticatedUserJSON(
                    "other.customer@gmail.com", Set.of(Role.CUSTOMER));
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING + "/{id}").build(reviewId);
            RequestEntity<String> request = RequestEntity.put(uri)
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedUser)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .body(json);

            ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }
    }

    @Nested
    class Delete {
        @Test
        void shouldReturn200_WhenReviewDeletedSuccessfully() {
            String customerEmail = "bekrenov.s@gmail.com";
            String reviewId = findAnyReviewForCustomer(customerEmail).getId();
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING + "/{id}").build(reviewId);
            String authenticatedUser = TestUtil.getAuthenticatedUserJSON(customerEmail, Set.of(Role.CUSTOMER));
            RequestEntity<Void> request = RequestEntity.delete(uri)
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedUser)
                    .build();

            ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturn200_WhenAuthenticatedUserIsEmployee() {
            String reviewId = findAnyReviewForCustomer("bekrenov.roman@gmail.com").getId();
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING + "/{id}").build(reviewId);
            String authenticatedUser = TestUtil.getAuthenticatedUserJSON("employee.email@example.com", Set.of(Role.EMPLOYEE));
            RequestEntity<Void> request = RequestEntity.delete(uri)
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedUser)
                    .build();

            ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturn404_WhenReviewNotFound() {
            String reviewId = "does_not_exist";
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING + "/{id}").build(reviewId);
            RequestEntity<Void> request = RequestEntity.delete(uri).build();

            ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        void shouldReturn403_WhenCustomerIsNotReviewOwner() {
            String reviewId = findAnyReviewForCustomer("bekrenov.s@gmail.com").getId();
            URI uri = UriComponentsBuilder.fromPath(URI_MAPPING + "/{id}").build(reviewId);
            String authenticatedUser = TestUtil.getAuthenticatedUserJSON(
                    "other.customer@example.com", Set.of(Role.CUSTOMER));
            RequestEntity<Void> request = RequestEntity.delete(uri)
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedUser)
                    .build();

            ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }
    }

    private static FeignException mockFeignException(HttpStatus status) {
        FeignException ex = mock(FeignException.class);
        when(ex.status()).thenReturn(status.value());
        return ex;
    }

    void mockCustomersProxy() {
        when(customersProxy.getCustomerByEmail(anyString())).thenReturn(ResponseEntity.ok(Map.of()));
    }

    Review findAnyReviewForCustomer(String customerEmail) {
        return reviewRepository.findAllByCustomerEmail(customerEmail)
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Review not found"));
    }
}
