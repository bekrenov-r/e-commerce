package com.bekrenovr.ecommerce.orders.service;

import com.bekrenovr.ecommerce.common.util.PageUtil;
import com.bekrenovr.ecommerce.orders.dto.mapper.DeliveryMapper;
import com.bekrenovr.ecommerce.orders.dto.mapper.OrderMapper;
import com.bekrenovr.ecommerce.orders.dto.request.OrderRequest;
import com.bekrenovr.ecommerce.orders.dto.response.OrderDetailedResponse;
import com.bekrenovr.ecommerce.orders.dto.response.OrderResponse;
import com.bekrenovr.ecommerce.orders.model.entity.Delivery;
import com.bekrenovr.ecommerce.orders.model.entity.ItemEntry;
import com.bekrenovr.ecommerce.orders.model.entity.Order;
import com.bekrenovr.ecommerce.orders.model.enums.OrderStatus;
import com.bekrenovr.ecommerce.orders.proxy.CustomerProxy;
import com.bekrenovr.ecommerce.orders.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CustomerProxy customerProxy;
    private final DeliveryMapper deliveryMapper;
    private final ItemEntryService itemEntryService;

    public OrderDetailedResponse getById(UUID id) {
        Order order = orderRepository.findByIdOrThrowDefault(id);
        return orderMapper.entityToDetailedResponse(order);
    }

    public Page<OrderResponse> getAllByCustomerEmail(String customerEmail, int pageNumber, int pageSize) {
        List<OrderResponse> orders = orderRepository.findAllByCustomerEmail(customerEmail)
                .stream()
                .map(orderMapper::entityToResponse)
                .toList();
        return PageUtil.paginateList(orders, pageNumber, pageSize);
    }

    public OrderResponse createOrder(OrderRequest orderRequest) {
        String customerEmail = null; // todo: get customer email from jwt
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

        return orderMapper.entityToResponse(orderRepository.save(order));
    }



    private double calculateTotalPrice(List<ItemEntry> itemEntries) {
        return itemEntries.stream()
                .map(ItemEntry::getTotalPrice)
                .reduce(0.0, Double::sum);
    }

    private double calculateTotalPriceAfterDiscount(List<ItemEntry> itemEntries) {
        return itemEntries.stream()
                .map(ItemEntry::getTotalPriceAfterDiscount)
                .reduce(0.0, Double::sum);
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
}
