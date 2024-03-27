package com.bekrenovr.ecommerce.catalog.dto.response;

import com.bekrenovr.ecommerce.catalog.model.enums.Color;

import java.util.UUID;

public record ItemResponse(
        UUID id,
        String name,
        Double price,
        Double discount,
        Double priceAfterDiscount,
        String brand,
        Double rating,
        Color color,
        boolean isOnWishList,
        boolean isNew,
        boolean isPopular

) {
}
