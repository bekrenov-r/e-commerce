package com.bekrenovr.ecommerce.orders.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class ItemEntryResponse {
    private UUID id;
    private UUID itemId;
    private String itemName;
    private String itemSize;
    private int quantity;
    private double discount;
    private double itemPrice;
    private double itemPriceAfterDiscount;
    private double totalPrice;
    private double totalPriceAfterDiscount;
}