package com.bekrenovr.ecommerce.catalog.dto.response;

import com.bekrenovr.ecommerce.catalog.model.ColorEnum;

import java.util.List;

public record ItemResponse(
        Long id,
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
