package com.bekrenovr.ecommerce.catalog.item.uniqueitem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record UniqueItemDTO(
        @NotBlank
        String size,
        @PositiveOrZero
        int quantity
) { }
