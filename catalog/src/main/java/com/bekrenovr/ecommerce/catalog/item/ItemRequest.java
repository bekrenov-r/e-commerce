package com.bekrenovr.ecommerce.catalog.item;

import com.bekrenovr.ecommerce.catalog.item.filters.Color;
import com.bekrenovr.ecommerce.catalog.item.filters.Gender;
import com.bekrenovr.ecommerce.catalog.item.filters.Material;
import com.bekrenovr.ecommerce.catalog.item.filters.Season;
import com.bekrenovr.ecommerce.catalog.item.uniqueitem.UniqueItemDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.UUID;

public record ItemRequest(
        @NotBlank
        String name,
        String description,
        @Positive
        double price,
        @PositiveOrZero @DecimalMax(value = "1.0")
        double discount,
        @NotNull
        UUID categoryId,
        UUID subcategoryId,
        @NotNull
        Color color,
        @NotNull
        Gender gender,
        @NotNull
        UUID brandId,
        @NotNull
        String itemCode,
        Material material,
        Season season,
        List<@Valid UniqueItemDTO> uniqueItems
) {
}
