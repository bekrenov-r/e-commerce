package com.bekrenovr.ecommerce.orders.dto.response;

import com.bekrenovr.ecommerce.orders.model.enums.OrderStatus;

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
