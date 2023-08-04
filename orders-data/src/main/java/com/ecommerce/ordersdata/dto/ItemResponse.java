package com.ecommerce.ordersdata.dto;

public record ItemResponse(Integer id, double price, double priceAfterDiscount, String producer, String description) {
}
