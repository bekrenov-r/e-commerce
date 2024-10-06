package com.bekrenovr.ecommerce.orders.service;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import com.bekrenovr.ecommerce.common.security.AuthenticatedUser;
import com.bekrenovr.ecommerce.common.security.AuthenticationUtil;
import com.bekrenovr.ecommerce.common.security.Role;
import com.bekrenovr.ecommerce.common.util.PageUtil;
import com.bekrenovr.ecommerce.orders.dto.mapper.DeliveryMapper;
import com.bekrenovr.ecommerce.orders.dto.mapper.OrderMapper;
import com.bekrenovr.ecommerce.orders.dto.request.CustomerRequest;
import com.bekrenovr.ecommerce.orders.dto.request.OrderRequest;
import com.bekrenovr.ecommerce.orders.dto.response.OrderDetailedResponse;
import com.bekrenovr.ecommerce.orders.dto.response.OrderResponse;
import com.bekrenovr.ecommerce.orders.model.OrderEvent;
import com.bekrenovr.ecommerce.orders.model.entity.Delivery;
import com.bekrenovr.ecommerce.orders.model.entity.ItemEntry;
import com.bekrenovr.ecommerce.orders.model.entity.Order;
import com.bekrenovr.ecommerce.orders.model.enums.OrderStatus;
import com.bekrenovr.ecommerce.orders.proxy.CustomerProxy;
import com.bekrenovr.ecommerce.orders.repository.OrderRepository;
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
import java.util.UUID;

import static com.bekrenovr.ecommerce.orders.exception.OrdersApplicationExceptionReason.CUSTOMER_IS_NOT_ORDER_OWNER;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CustomerProxy customerProxy;
    private final DeliveryMapper deliveryMapper;
    private final ItemEntryService itemEntryService;
    private final OrderEventProducer orderEventProducer;

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

    public OrderResponse createOrder(OrderRequest orderRequest) {
        String customerEmail = resolveCustomer(orderRequest);
        List<ItemEntry> itemEntries = itemEntryService.createItemEntries(orderRequest.itemEntries());
        Delivery delivery = deliveryMapper.requestToEntity(orderRequest.delivery());
        Order order = Order.builder()
                .customerEmail(customerEmail)
                .itemEntries(itemEntries)
                .delivery(delivery)
                .createdAt(LocalDateTime.now())
                .lastUpdatedAt(LocalDateTime.now())
                .totalPrice(calculateTotalPrice(itemEntries))
                .totalPriceAfterDiscount(calculateTotalPriceAfterDiscount(itemEntries))
                .number(generateOrderNumber())
                .status(OrderStatus.ACCEPTED)
                .build();
        Order savedOrder = orderRepository.save(order);
        orderEventProducer.send(new OrderEvent(savedOrder.getId(), savedOrder.getStatus(), orderRequest.itemEntries()));
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
            customerProxy.createCustomer(customer);
        } catch(FeignException.Conflict ex) {
            // do nothing since 409 means that customer already exists and can be used
        }
        return request.customer().getEmail();
    }

    private double calculateTotalPrice(List<ItemEntry> itemEntries) {
        return itemEntries.stream()
                .map(ItemEntry::getTotalPrice)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_DOWN)
                .doubleValue();
    }

    private double calculateTotalPriceAfterDiscount(List<ItemEntry> itemEntries) {
        return itemEntries.stream()
                .map(ItemEntry::getTotalPriceAfterDiscount)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
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
}
