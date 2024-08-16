package com.bekrenovr.ecommerce.orders.dto.request;

import com.bekrenovr.ecommerce.catalog.model.Size;
import com.bekrenovr.ecommerce.orders.util.SizeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.UUID;

public record ItemEntryRequest(
        UUID itemId,
        int quantity,
        @JsonDeserialize(using = SizeDeserializer.class)
        Size size
) { }
