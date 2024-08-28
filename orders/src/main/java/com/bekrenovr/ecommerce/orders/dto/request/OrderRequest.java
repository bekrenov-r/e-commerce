package com.bekrenovr.ecommerce.orders.dto.request;

import com.bekrenovr.ecommerce.orders.validation.CustomerInOrderRequestConstraint;
import jakarta.validation.Valid;

import java.util.List;

public record OrderRequest(
    List<ItemEntryRequest> itemEntries,
    DeliveryRequest delivery,
    @Valid @CustomerInOrderRequestConstraint
    CustomerRequest customer
) { }
