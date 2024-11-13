package com.bekrenovr.ecommerce.orders.order;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import com.bekrenovr.ecommerce.common.security.AuthenticatedUser;
import com.bekrenovr.ecommerce.common.security.AuthenticationUtil;
import com.bekrenovr.ecommerce.common.security.Role;
import com.bekrenovr.ecommerce.common.util.PageUtil;
import com.bekrenovr.ecommerce.orders.delivery.Delivery;
import com.bekrenovr.ecommerce.orders.delivery.DeliveryMapper;
import com.bekrenovr.ecommerce.orders.feign.CustomersProxy;
import com.bekrenovr.ecommerce.orders.order.dto.*;
import com.bekrenovr.ecommerce.orders.order.event.OrderEvent;
import com.bekrenovr.ecommerce.orders.order.event.OrderEventProducer;
import com.bekrenovr.ecommerce.orders.order.itementry.ItemEntry;
import com.bekrenovr.ecommerce.orders.order.itementry.ItemEntryMapper;
import com.bekrenovr.ecommerce.orders.order.itementry.ItemEntryRequest;
import com.bekrenovr.ecommerce.orders.order.itementry.ItemEntryService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.bekrenovr.ecommerce.orders.exception.OrdersApplicationExceptionReason.CANNOT_CANCEL_ORDER;
import static com.bekrenovr.ecommerce.orders.exception.OrdersApplicationExceptionReason.CUSTOMER_IS_NOT_ORDER_OWNER;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CustomersProxy customersProxy;
    private final DeliveryMapper deliveryMapper;
    private final ItemEntryService itemEntryService;
    private final OrderEventProducer orderEventProducer;
    private final ItemEntryMapper itemEntryMapper;

    public OrderDetailedResponse getById(UUID id) {
        Order order = orderRepository.findByIdOrThrowDefault(id);
        requireOrderOwnershipByCustomer(order);
        return orderMapper.entityToDetailedResponse(order);
    }

    public Page<OrderResponse> getAllForCustomer(OrderStatus status, int pageNumber, int pageSize) {
        String customerEmail = AuthenticationUtil.getAuthenticatedUser().getUsername();
        List<OrderResponse> orders = this.findOrders(customerEmail, status)
                .stream()
                .map(orderMapper::entityToResponse)
                .toList();
        return PageUtil.paginateList(orders, pageNumber, pageSize);
    }

    public OrderResponse create(OrderRequest orderRequest) {
        String customerEmail = resolveCustomer(orderRequest);
        Map<ItemEntryRequest, CatalogItem> itemEntryRequestToCatalogItem =
                itemEntryService.processRequests(orderRequest.itemEntries());
        List<ItemEntry> itemEntries = orderRequest.itemEntries().stream()
                .map(itemEntryMapper::requestToEntity)
                .toList();
        Delivery delivery = deliveryMapper.requestToEntity(orderRequest.delivery());
        double totalPrice = calculateTotalPrice(itemEntryRequestToCatalogItem);
        double totalPriceAfterDiscount = calculateTotalPriceAfterDiscount(itemEntryRequestToCatalogItem);
        Order order = Order.builder()
                .customerEmail(customerEmail)
                .itemEntries(itemEntries)
                .delivery(delivery)
                .createdAt(LocalDateTime.now())
                .lastUpdatedAt(LocalDateTime.now())
                .totalPrice(totalPrice)
                .totalPriceAfterDiscount(totalPriceAfterDiscount)
                .number(generateOrderNumber())
                .status(OrderStatus.ACCEPTED)
                .build();
        Order savedOrder = orderRepository.save(order);
        orderEventProducer.produce(new OrderEvent(savedOrder.getId(), savedOrder.getStatus(), orderRequest.itemEntries()));
        return orderMapper.entityToResponse(savedOrder);
    }

    private List<Order> findOrders(String customerEmail, OrderStatus status) {
        return status != null
                ? orderRepository.findAllByCustomerEmailAndStatus(customerEmail, status)
                : orderRepository.findAllByCustomerEmail(customerEmail);
    }

    private String resolveCustomer(OrderRequest request){
        if(AuthenticationUtil.requestHasAuthentication()){
            return AuthenticationUtil.getAuthenticatedUser().getUsername();
        }
        try {
            CustomerRequest customer = request.customer();
            customer.setRegistered(false);
            customersProxy.createCustomer(customer);
        } catch(FeignException.Conflict ex) {
            // do nothing since 409 means that customer already exists and can be used
        }
        return request.customer().getEmail();
    }

    private double calculateTotalPrice(Map<ItemEntryRequest, CatalogItem> catalogItemsMap) {
        return catalogItemsMap.entrySet().stream()
                .map(mapEntry -> {
                    int quantity = mapEntry.getKey().quantity();
                    double itemPrice = mapEntry.getValue().price();
                    return BigDecimal.valueOf(itemPrice)
                            .multiply(BigDecimal.valueOf(quantity));
                }).reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_DOWN)
                .doubleValue();
    }

    private double calculateTotalPriceAfterDiscount(Map<ItemEntryRequest, CatalogItem> catalogItemsMap) {
        return catalogItemsMap.entrySet().stream()
                .map(mapEntry -> {
                    int quantity = mapEntry.getKey().quantity();
                    double itemPrice = mapEntry.getValue().priceAfterDiscount();
                    return BigDecimal.valueOf(itemPrice)
                            .multiply(BigDecimal.valueOf(quantity));
                }).reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_DOWN)
                .doubleValue();
    }

    private String generateOrderNumber() {
        LocalDate today = LocalDate.now();
        return new StringBuilder()
                .append(today.getYear())
                .append(String.format("%02d", today.getMonthValue()))
                .append(String.format("%02d", today.getDayOfMonth()))
                .append("_")
                .append(RandomStringUtils.randomNumeric(7))
                .toString();
    }

    private void requireOrderOwnershipByCustomer(Order order){
        AuthenticatedUser user = AuthenticationUtil.getAuthenticatedUser();
        if(user.hasRole(Role.CUSTOMER) && !order.getCustomerEmail().equals(user.getUsername())) {
            throw new EcommerceApplicationException(CUSTOMER_IS_NOT_ORDER_OWNER, user.getUsername());
        }
    }

    public void cancel(UUID id) {
        Order order = orderRepository.findByIdOrThrowDefault(id);
        requireOrderOwnershipByCustomer(order);
        validateOrderStatusBeforeCancellation(order);
        order.setStatus(OrderStatus.CANCELLED);
        Order savedOrder = orderRepository.save(order);
        this.publishCancellationEvent(savedOrder);
    }

    private void publishCancellationEvent(Order order) {
        List<ItemEntryRequest> itemEntries = order.getItemEntries()
                .stream()
                .map(ie -> new ItemEntryRequest(ie.getItemId(), ie.getQuantity(), ie.getItemSize()))
                .toList();
        orderEventProducer.produce(new OrderEvent(order.getId(), order.getStatus(), itemEntries));
    }

    private void validateOrderStatusBeforeCancellation(Order order) {
        OrderStatus status = order.getStatus();
        if(!(status.equals(OrderStatus.ACCEPTED) || status.equals(OrderStatus.SHIPPING))) {
            throw new EcommerceApplicationException(CANNOT_CANCEL_ORDER, status);
        }
    }
}
