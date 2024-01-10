package com.bekrenovr.ecommerce.orders.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderRequest(
        @Positive(message = "Customer id must be positive")
        @NotNull(message = "Customer id can't be null")
        Integer customerId,
        @Positive(message = "Item id must be positive")
        @NotNull(message = "Item id can't be null")
        Integer itemId,
        @Positive(message = "Quantity must be positive")
        @NotNull(message = "Quantity can't be null")
        Integer quantity) {
}
