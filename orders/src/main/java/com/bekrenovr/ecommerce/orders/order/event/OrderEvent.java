package com.bekrenovr.ecommerce.orders.order.event;

import com.bekrenovr.ecommerce.orders.order.OrderStatus;
import com.bekrenovr.ecommerce.orders.order.itementry.ItemEntryRequest;

import java.util.List;
import java.util.UUID;

public record OrderEvent(UUID orderId, OrderStatus status, List<ItemEntryRequest> itemEntries) { }
