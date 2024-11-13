package com.bekrenovr.ecommerce.orders.order.dto;

import com.bekrenovr.ecommerce.orders.delivery.DeliveryResponse;
import com.bekrenovr.ecommerce.orders.order.OrderStatus;
import com.bekrenovr.ecommerce.orders.order.itementry.ItemEntryResponse;

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
