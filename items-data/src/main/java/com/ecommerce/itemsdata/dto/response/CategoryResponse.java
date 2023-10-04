package com.ecommerce.itemsdata.dto.response;

public record CategoryResponse(
        Long id,
        String name,
        byte[] image
) {}
