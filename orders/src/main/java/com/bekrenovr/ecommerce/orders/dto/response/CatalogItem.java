package com.bekrenovr.ecommerce.orders.dto.response;

import java.util.List;
import java.util.UUID;

public record CatalogItem(
        UUID id,
        String name,
        double discount,
        double price,
        double priceAfterDiscount,
        List<UniqueItemResponse> uniqueItems
) { }
