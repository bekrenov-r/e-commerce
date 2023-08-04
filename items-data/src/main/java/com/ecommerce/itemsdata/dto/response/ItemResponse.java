package com.ecommerce.itemsdata.dto.response;

import com.ecommerce.itemsdata.model.Color;
import com.ecommerce.itemsdata.model.ColorEnum;

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
