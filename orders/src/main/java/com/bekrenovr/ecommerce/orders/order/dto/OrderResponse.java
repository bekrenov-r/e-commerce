package com.bekrenovr.ecommerce.orders.order.dto;

import com.bekrenovr.ecommerce.orders.order.OrderStatus;
import com.bekrenovr.ecommerce.orders.order.itementry.ItemEntryResponse;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        Set<ItemEntryResponse> itemEntries,
        LocalDateTime createdAt,
        double totalPrice,
        double totalPriceAfterDiscount,
        OrderStatus status,
        String number
) { }
