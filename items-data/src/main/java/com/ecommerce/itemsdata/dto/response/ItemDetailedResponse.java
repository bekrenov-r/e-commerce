package com.ecommerce.itemsdata.dto.response;

import com.ecommerce.itemsdata.model.Color;
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
        List<Color> colors,
        List<Color> availableColors,
        List<byte[]> images,
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
