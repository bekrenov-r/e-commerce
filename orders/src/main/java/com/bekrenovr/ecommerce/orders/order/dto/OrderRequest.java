package com.bekrenovr.ecommerce.orders.order.dto;

import com.bekrenovr.ecommerce.orders.delivery.DeliveryRequest;
import com.bekrenovr.ecommerce.orders.order.itementry.ItemEntryRequest;
import com.bekrenovr.ecommerce.orders.order.validation.CustomerInOrderRequestConstraint;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderRequest(
    @NotEmpty
    List<ItemEntryRequest> itemEntries,
    DeliveryRequest delivery,
    @CustomerInOrderRequestConstraint
    CustomerRequest customer
) { }
