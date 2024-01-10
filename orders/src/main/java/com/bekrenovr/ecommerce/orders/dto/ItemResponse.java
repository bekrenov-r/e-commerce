package com.bekrenovr.ecommerce.orders.dto;

public record ItemResponse(Integer id, double price, double priceAfterDiscount, String producer, String description) {
}
