package com.bekrenovr.ecommerce.orders.model;

import com.bekrenovr.ecommerce.orders.dto.request.ItemEntryRequest;
import com.bekrenovr.ecommerce.orders.model.enums.OrderStatus;

import java.util.List;
import java.util.UUID;

public record OrderEvent(UUID orderId, OrderStatus status, List<ItemEntryRequest> itemEntries) { }
