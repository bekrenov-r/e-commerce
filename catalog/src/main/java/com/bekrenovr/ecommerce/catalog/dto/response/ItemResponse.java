package com.bekrenovr.ecommerce.catalog.dto.response;

import com.bekrenovr.ecommerce.catalog.model.ColorEnum;

import java.util.List;
import java.util.UUID;

public record ItemResponse(
        UUID id,
        String name,
        Double price,
        Double discount,
        Double priceAfterDiscount,
        List<byte[]> images,
        String brand,
        Double rating,
        List<ColorEnum> colors,
        boolean isOnWishList,
        boolean isNew,
        boolean isPopular

) {
}
