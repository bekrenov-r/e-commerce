package com.bekrenovr.ecommerce.catalog.dto.response;

public record CategoryResponse(
        String id,
        String name,
        byte[] image
) {}
