package com.bekrenovr.ecommerce.orders.service;

import com.bekrenovr.ecommerce.catalog.dto.response.ItemResponse;
import com.bekrenovr.ecommerce.catalog.dto.response.UniqueItemShortResponse;
import com.bekrenovr.ecommerce.catalog.model.Size;
import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import com.bekrenovr.ecommerce.common.util.PageUtil;
import com.bekrenovr.ecommerce.orders.dto.mapper.DeliveryMapper;
import com.bekrenovr.ecommerce.orders.dto.mapper.ItemEntryMapper;
import com.bekrenovr.ecommerce.orders.dto.mapper.OrderMapper;
import com.bekrenovr.ecommerce.orders.dto.request.ItemEntryRequest;
import com.bekrenovr.ecommerce.orders.dto.request.OrderRequest;
import com.bekrenovr.ecommerce.orders.dto.response.OrderDetailedResponse;
import com.bekrenovr.ecommerce.orders.dto.response.OrderResponse;
import com.bekrenovr.ecommerce.orders.model.entity.Delivery;
import com.bekrenovr.ecommerce.orders.model.entity.ItemEntry;
import com.bekrenovr.ecommerce.orders.model.entity.Order;
import com.bekrenovr.ecommerce.orders.model.enums.OrderStatus;
import com.bekrenovr.ecommerce.orders.proxy.CustomerProxy;
import com.bekrenovr.ecommerce.orders.proxy.ItemProxy;
import com.bekrenovr.ecommerce.orders.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.bekrenovr.ecommerce.orders.exception.OrdersApplicationExceptionReason.*;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CustomerProxy customerProxy;
    private final ItemProxy itemProxy;
    private final ItemEntryMapper itemEntryMapper;
    private final DeliveryMapper deliveryMapper;

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
        List<UUID> itemsIds = orderRequest.itemEntries().stream()
                .map(ItemEntryRequest::itemId)
                .toList();
        List<ItemResponse> items = itemProxy.getItemsByIds(itemsIds).getBody();
        if(items.size() < orderRequest.itemEntries().size())
            throw new EcommerceApplicationException(NON_EXISTENT_ITEMS_IN_ORDER);
        List<ItemEntry> itemEntries = new ArrayList<>();
        for(int i = 0; i < items.size(); i++) {
            validateEntryAgainstItem(orderRequest.itemEntries().get(i), items.get(i));
            int quantity = orderRequest.itemEntries().get(i).quantity();
            Size size = orderRequest.itemEntries().get(i).size();
            ItemEntry itemEntry = itemEntryMapper.itemResponseToEntity(items.get(i), quantity, size);
            itemEntries.add(itemEntry);
        }
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

    private void validateEntryAgainstItem(ItemEntryRequest itemEntry, ItemResponse item) {
        Size requestedSize = itemEntry.size();
        int requestedQuantity = itemEntry.quantity();
        UniqueItemShortResponse uniqueItemBySize = item.uniqueItems().stream()
                .filter(uniqueItem -> uniqueItem.size().equals(requestedSize.getSizeValue()))
                .findFirst()
                .orElseThrow(() -> new EcommerceApplicationException(SIZE_IS_UNAVAILABLE, requestedSize.getSizeValue(), item.id()));
        if(uniqueItemBySize.quantity() < requestedQuantity)
            throw new EcommerceApplicationException(QUANTITY_IS_UNAVAILABLE, item.id(), requestedSize.getSizeValue());
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
