package com.bekrenovr.ecommerce.catalog.kafka.order.model;

import java.util.UUID;

public record OrderItemEntry(UUID itemId, int quantity, String size) { }
