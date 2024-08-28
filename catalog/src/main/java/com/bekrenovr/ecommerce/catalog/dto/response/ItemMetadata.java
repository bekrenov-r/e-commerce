package com.bekrenovr.ecommerce.catalog.dto.response;

import lombok.Data;

@Data
public class ItemMetadata {
    boolean isOnWishList;
    boolean isNew;
    boolean isPopular;
    boolean isAvailable;
}
