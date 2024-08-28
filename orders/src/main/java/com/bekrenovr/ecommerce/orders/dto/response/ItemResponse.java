package com.bekrenovr.ecommerce.orders.dto.response;

import java.util.List;
import java.util.UUID;

public record ItemResponse(
        UUID id,
        String name,
        String size,
        int quantity,
        double discount,
        double price,
        double priceAfterDiscount,
        List<UniqueItemResponse> uniqueItems
) { }
