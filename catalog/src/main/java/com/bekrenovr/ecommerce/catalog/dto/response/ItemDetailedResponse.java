package com.bekrenovr.ecommerce.catalog.dto.response;

import com.bekrenovr.ecommerce.catalog.model.Color;
import org.hibernate.engine.jdbc.Size;

import java.util.List;

public record ItemDetailedResponse(
        String name,
        String description,
        Double price,
        Double discount,
        Double priceAfterDiscount,
        List<Size> sizes,
        List<Size> availableSizes,
        Color color,
        String itemCode,
        List<ReviewResponse> reviews,
        List<ItemResponse> similarItems,
        boolean isOnWishList,
        boolean isAvailable,
        boolean isNew,
        boolean isPopular,
        String brand,
        Double rating,
        Integer reviewsCount
) {
}
