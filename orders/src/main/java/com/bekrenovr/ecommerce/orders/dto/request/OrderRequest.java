package com.bekrenovr.ecommerce.orders.dto.request;

import com.bekrenovr.ecommerce.orders.validation.CustomerInOrderRequestConstraint;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderRequest(
    @NotEmpty
    List<ItemEntryRequest> itemEntries,
    DeliveryRequest delivery,
    @CustomerInOrderRequestConstraint
    CustomerRequest customer
) { }
