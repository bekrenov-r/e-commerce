package com.bekrenovr.ecommerce.catalog.dto.response;

import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name
) {}
