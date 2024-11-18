package com.bekrenovr.ecommerce.catalog.item;

import com.bekrenovr.ecommerce.catalog.item.filters.Color;
import com.bekrenovr.ecommerce.catalog.item.filters.Gender;
import com.bekrenovr.ecommerce.catalog.item.filters.Material;
import com.bekrenovr.ecommerce.catalog.item.filters.Season;
import com.bekrenovr.ecommerce.catalog.item.uniqueitem.UniqueItemDTO;

import java.util.List;
import java.util.UUID;

public record ItemRequest(
        String name,
        String description,
        double price,
        double discount,
        UUID categoryId,
        UUID subcategoryId,
        Color color,
        Gender gender,
        UUID brandId,
        String itemCode,
        Material material,
        Season season,
        List<UniqueItemDTO> uniqueItems
) {
}
