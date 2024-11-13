package com.bekrenovr.ecommerce.catalog.category;

import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name
) {}
