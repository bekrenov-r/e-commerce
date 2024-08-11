package com.bekrenovr.ecommerce.orders.dto.response;

import com.bekrenovr.ecommerce.orders.model.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderDetailedResponse(
        UUID id,
        List<ItemEntryResponse> itemEntries,
        DeliveryResponse delivery,
        LocalDateTime createdAt,
        double totalPrice,
        double totalPriceAfterDiscount,
        String number,
        OrderStatus status
) { }
