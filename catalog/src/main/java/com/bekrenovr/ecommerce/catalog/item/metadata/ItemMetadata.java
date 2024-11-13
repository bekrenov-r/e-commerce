package com.bekrenovr.ecommerce.catalog.item.metadata;

import lombok.Data;

@Data
public class ItemMetadata {
    boolean isOnWishList;
    boolean isNew;
    boolean isPopular;
    boolean isAvailable;
}
