package com.bekrenovr.ecommerce.orders.integration;

import com.bekrenovr.ecommerce.common.security.Role;
import com.bekrenovr.ecommerce.common.security.SecurityConstants;
import com.bekrenovr.ecommerce.common.util.TestUtil;
import com.bekrenovr.ecommerce.orders.feign.CatalogProxy;
import com.bekrenovr.ecommerce.orders.feign.CustomersProxy;
import com.bekrenovr.ecommerce.orders.order.Order;
import com.bekrenovr.ecommerce.orders.order.OrderRepository;
import com.bekrenovr.ecommerce.orders.order.OrderStatus;
import com.bekrenovr.ecommerce.orders.order.dto.CatalogItem;
import com.bekrenovr.ecommerce.orders.order.dto.OrderResponse;
import com.bekrenovr.ecommerce.orders.order.dto.UniqueItemResponse;
import com.bekrenovr.ecommerce.orders.order.event.OrderEvent;
import com.bekrenovr.ecommerce.orders.order.event.OrderEventProducer;
import com.bekrenovr.ecommerce.orders.util.OrderJsonBuilder;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpHeaders;
import org.json.JSONException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.bekrenovr.ecommerce.orders.datasets.CatalogItems.catalogItem;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class OrderIT {
    TestRestTemplate restTemplate;
    OrderRepository orderRepository;

    @MockBean
    CatalogProxy catalogProxy;
    @MockBean
    CustomersProxy customersProxy;
    @MockBean
    OrderEventProducer eventProducer;

    @Autowired
    OrderIT(TestRestTemplate restTemplate, OrderRepository orderRepository) {
        this.restTemplate = restTemplate;
        this.orderRepository = orderRepository;
    }

    @Nested
    class GetById {
        @Test
        void shouldReturn200_WhenOrderExists_AndAuthenticatedCustomerIsOrderOwner() {
            UUID orderId = UUID.fromString("4c17c3c9-97a2-45ba-aaf4-d0d1fff56078");
            when(catalogProxy.getItemById(any(UUID.class)))
                    .thenReturn(ResponseEntity.ok(catalogItem().build()));
            String authenticatedUser = TestUtil.getAuthenticatedUserJSON("bekrenov.s@gmail.com", Set.of(Role.CUSTOMER));
            RequestEntity<Void> request = RequestEntity.get(URI.create("/" + orderId))
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedUser)
                    .build();

            ResponseEntity<?> response = restTemplate.exchange(request, Object.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturn403_WhenOrderExists_AndAuthenticatedCustomerIsNotOrderOwner() {
            String orderId = "4c17c3c9-97a2-45ba-aaf4-d0d1fff56078";
            String authenticatedUser = TestUtil.getAuthenticatedUserJSON("not.order.owner@example.com", Set.of(Role.CUSTOMER));
            RequestEntity<Void> request = RequestEntity.get(URI.create("/" + orderId))
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedUser)
                    .build();

            ResponseEntity<?> response = restTemplate.exchange(request, Object.class);

            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }

        @Test
        void shouldReturn404_WhenOrderDoesNotExist() {
            String orderId = UUID.randomUUID().toString();
            String authenticatedUser = TestUtil.getAuthenticatedUserJSON("bekrenov.s@gmail.com", Set.of(Role.CUSTOMER));
            RequestEntity<Void> request = RequestEntity.get(URI.create("/" + orderId))
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedUser)
                    .build();

            ResponseEntity<?> response = restTemplate.exchange(request, Object.class);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    class GetAllForCustomer {
        @ParameterizedTest
        @EnumSource(OrderStatus.class)
        void shouldReturn200_WithOrderStatus(OrderStatus status) {
            when(catalogProxy.getItemById(any(UUID.class))).thenReturn(ResponseEntity.ok(catalogItem().build()));
            URI uri = UriComponentsBuilder.fromPath("/customer")
                    .queryParam("status", status)
                    .build().toUri();
            String authenticatedUser = TestUtil.getAuthenticatedUserJSON("bekrenov.s@gmail.com", Set.of(Role.CUSTOMER));
            RequestEntity<Void> request = RequestEntity.get(uri)
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedUser)
                    .build();

            ResponseEntity<?> response = restTemplate.exchange(request, Object.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturn200_WithoutOrderStatus() {
            when(catalogProxy.getItemById(any(UUID.class))).thenReturn(ResponseEntity.ok(catalogItem().build()));
            String authenticatedUser = TestUtil.getAuthenticatedUserJSON("bekrenov.s@gmail.com", Set.of(Role.CUSTOMER));
            RequestEntity<Void> request = RequestEntity.get(URI.create("/customer"))
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedUser)
                    .build();

            ResponseEntity<?> response = restTemplate.exchange(request, Object.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturn200AndEmptyPage_WhenAuthenticatedCustomerDoesntHaveOrders() {
            String authenticatedUser = TestUtil.getAuthenticatedUserJSON("doesnt.have.orders@gmail.com", Set.of(Role.CUSTOMER));
            RequestEntity<Void> request = RequestEntity.get(URI.create("/customer"))
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedUser)
                    .build();

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());

            DocumentContext json = JsonPath.parse(response.getBody());
            assertTrue(json.read("$.content", List.class).isEmpty());
            assertEquals(0, json.read("$.totalElements", Integer.class));
            assertEquals(0, json.read("$.totalPages", Integer.class));
            assertTrue(json.read("$.empty", Boolean.class));
        }
    }

    @Nested
    class Create {
        @Test
        void shouldReturn201AndOrderResponse_WhenOrderCreatedWithAuthenticatedCustomer() throws JSONException {
            CatalogItem item1 = catalogItem()
                    .uniqueItems(List.of(new UniqueItemResponse("XL", 10)))
                    .build();
            CatalogItem item2 = catalogItem()
                    .uniqueItems(List.of(new UniqueItemResponse("XS", 10)))
                    .build();
            mockCatalogProxy(List.of(item1, item2));

            String requestBody = OrderJsonBuilder.create()
                    .itemEntry(item1.id(), "XL", 2)
                    .itemEntry(item2.id(), "XS", 1)
                    .build().toString();
            String authenticatedUser = TestUtil.getAuthenticatedUserJSON("bekrenov.s@gmail.com", Set.of(Role.CUSTOMER));
            RequestEntity<String> request = RequestEntity.post("/")
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedUser)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .body(requestBody);

            ResponseEntity<OrderResponse> response = restTemplate.exchange(request, OrderResponse.class);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
        }

        @Test
        void shouldReturn201_WhenOrderCreatedWithCustomerInRequestBody() throws JSONException {
            CatalogItem item1 = catalogItem()
                    .uniqueItems(List.of(new UniqueItemResponse("XL", 10)))
                    .build();
            CatalogItem item2 = catalogItem()
                    .uniqueItems(List.of(new UniqueItemResponse("XS", 10)))
                    .build();
            mockCatalogProxy(List.of(item1, item2));

            String requestBody = OrderJsonBuilder.create()
                    .itemEntry(item1.id(), "XL", 2)
                    .itemEntry(item2.id(),"XS", 1)
                    .customer(c -> c.firstName("John")
                            .lastName("Doe")
                            .email("not.registered@example.com")
                    ).build().toString();
            RequestEntity<String> request = RequestEntity.post("/")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .body(requestBody);

            ResponseEntity<OrderResponse> response = restTemplate.exchange(request, OrderResponse.class);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
        }

        @Test
        void shouldReturn400_WhenNoAuthenticatedCustomer_AndNoCustomerInRequestBody() throws JSONException {
            String requestBody = OrderJsonBuilder.create().build().toString();
            RequestEntity<String> request = RequestEntity.post("/")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .body(requestBody);

            ResponseEntity<OrderResponse> response = restTemplate.exchange(request, OrderResponse.class);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

        @Test
        void shouldReturn400_WhenRequestHasNonExistentItems() throws JSONException {
            CatalogItem item = catalogItem()
                    .uniqueItems(List.of(new UniqueItemResponse("XL", 10)))
                    .build();
            String requestBody = OrderJsonBuilder.create()
                    .itemEntry(item.id(), "XL", 1)
                    .build().toString();
            when(catalogProxy.getItemsByIds(anyList())).thenReturn(ResponseEntity.ok(List.of()));
            String authenticatedCustomer = TestUtil.getAuthenticatedUserJSON("bekrenov.s@gmail.com", Set.of(Role.CUSTOMER));
            RequestEntity<String> request = RequestEntity.post("/")
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedCustomer)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .body(requestBody);

            ResponseEntity<?> response = restTemplate.exchange(request, Object.class);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

        private void mockCatalogProxy(List<CatalogItem> catalogItems) {
            when(catalogProxy.getItemsByIds(anyList())).thenReturn(ResponseEntity.ok(catalogItems));
            catalogItems.forEach(item ->
                    when(catalogProxy.getItemById(eq(item.id()))).thenReturn(ResponseEntity.ok(item))
            );
        }
    }

    @Nested
    class Cancel {
        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"ACCEPTED", "SHIPPING"})
        void shouldReturn200_WhenOrderCancellingOrderWithStatus(OrderStatus status) {
            doNothing().when(eventProducer).produce(any(OrderEvent.class));
            String customerEmail = "bekrenov.s@gmail.com";
            String orderId = this.findAnyOrderWithStatus(customerEmail, status).getId().toString();
            String authenticatedCustomer = TestUtil.getAuthenticatedUserJSON(customerEmail, Set.of(Role.CUSTOMER));
            RequestEntity<Void> request = RequestEntity.delete("/" + orderId)
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedCustomer)
                    .build();

            ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturn404_WhenOrderDoesNotExist() {
            String orderId = UUID.randomUUID().toString();
            String authenticatedCustomer = TestUtil.getAuthenticatedUserJSON("bekrenov.s@gmail.com", Set.of(Role.CUSTOMER));
            RequestEntity<Void> request = RequestEntity.delete("/" + orderId)
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedCustomer)
                    .build();

            ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"DELIVERED", "COMPLETED", "CANCELLED"})
        void shouldReturn400_WhenTryingToCancelOrderWithStatus(OrderStatus status) {
            String customerEmail = "bekrenov.s@gmail.com";
            String orderId = this.findAnyOrderWithStatus(customerEmail, status).getId().toString();
            String authenticatedCustomer = TestUtil.getAuthenticatedUserJSON(customerEmail, Set.of(Role.CUSTOMER));
            RequestEntity<Void> request = RequestEntity.delete("/" + orderId)
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedCustomer)
                    .build();

            ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

        @Test
        void shouldReturn403_WhenAuthenticatedCustomerIsNotOrderOwner() {
            String orderId = "4c17c3c9-97a2-45ba-aaf4-d0d1fff56078";
            String authenticatedCustomer = TestUtil.getAuthenticatedUserJSON("jane.doe@example.com", Set.of(Role.CUSTOMER));
            RequestEntity<Void> request = RequestEntity.delete("/" + orderId)
                    .header(SecurityConstants.AUTHENTICATED_USER_HEADER, authenticatedCustomer)
                    .build();

            ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);

            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }

        private Order findAnyOrderWithStatus(String customerEmail, OrderStatus status) {
            return orderRepository.findAllByCustomerEmailAndStatus(customerEmail, status).stream()
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("At least one order with status " + status.name() + " is required"));
        }
    }
}
