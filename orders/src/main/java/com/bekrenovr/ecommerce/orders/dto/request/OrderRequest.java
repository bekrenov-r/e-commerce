package com.bekrenovr.ecommerce.orders.dto.request;

import java.util.List;

public record OrderRequest(
    List<ItemEntryRequest> itemEntries,
    DeliveryRequest delivery
) { }
