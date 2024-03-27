package com.bekrenovr.ecommerce.catalog.dto.response;

import java.util.UUID;

public record ItemImageResponse(UUID id, byte[] image) { }
