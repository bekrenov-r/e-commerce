package com.bekrenovr.ecommerce.orders.dto.request;

import java.util.UUID;

public record ItemEntryRequest(
        UUID itemId,
        int quantity,
        String size
) { }
