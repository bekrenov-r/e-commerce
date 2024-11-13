package com.bekrenovr.ecommerce.orders.order.itementry;

import java.util.UUID;

public record ItemEntryRequest(
        UUID itemId,
        int quantity,
        String size
) { }
