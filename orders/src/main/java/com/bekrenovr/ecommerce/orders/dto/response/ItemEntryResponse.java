package com.bekrenovr.ecommerce.orders.dto.response;

import java.util.UUID;

public record ItemEntryResponse(
    UUID id,
    UUID itemId,
    String itemName,
    String itemSize,
    int quantity,
    double discount,
    double itemPrice,
    double itemPriceAfterDiscount,
    double totalPrice,
    double totalPriceAfterDiscount
) { }
