package com.bekrenovr.ecommerce.orders.datasets;

import com.bekrenovr.ecommerce.orders.dto.response.CatalogItem;
import com.bekrenovr.ecommerce.orders.dto.response.UniqueItemResponse;

import java.util.List;
import java.util.UUID;

public class CatalogItems {
    public static CatalogItem.CatalogItemBuilder catalogItem() {
        return catalogItem(UUID.randomUUID());
    }

    public static CatalogItem.CatalogItemBuilder catalogItem(UUID id) {
        return CatalogItem.builder()
                .id(id)
                .name("Sample catalog item")
                .discount(0.0)
                .price(100.0)
                .priceAfterDiscount(100.0)
                .uniqueItems(List.of(
                        new UniqueItemResponse("S", 10),
                        new UniqueItemResponse("M", 10),
                        new UniqueItemResponse("L", 10),
                        new UniqueItemResponse("XL", 10)
                ));
    }
}
