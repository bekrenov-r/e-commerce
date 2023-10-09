package com.ecommerce.itemsdata.dto.response;

public record CategoryResponse(
        String id,
        String name,
        byte[] image
) {}
