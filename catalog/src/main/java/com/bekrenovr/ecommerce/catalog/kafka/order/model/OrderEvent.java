package com.bekrenovr.ecommerce.catalog.kafka.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class OrderEvent {
    private UUID orderId;
    private OrderEventStatus status;
    private List<OrderItemEntry> itemEntries;
}
