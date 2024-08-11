package com.bekrenovr.ecommerce.orders.model.entity;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@NoArgsConstructor
@EqualsAndHashCode
public class ItemEntryId implements Serializable {
    private UUID orderId;
    private UUID itemId;
}
