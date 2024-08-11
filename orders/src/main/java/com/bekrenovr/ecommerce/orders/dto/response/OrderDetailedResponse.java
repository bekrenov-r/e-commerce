package com.bekrenovr.ecommerce.orders.dto.response;

import com.bekrenovr.ecommerce.orders.model.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record OrderDetailedResponse(
        UUID id,
        Set<ItemEntryResponse> itemEntries,
        DeliveryResponse delivery,
        LocalDateTime createdAt,
        double totalPrice,
        String number,
        OrderStatus status
) { }
