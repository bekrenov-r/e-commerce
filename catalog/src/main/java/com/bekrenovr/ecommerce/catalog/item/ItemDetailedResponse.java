package com.bekrenovr.ecommerce.catalog.item;

import com.bekrenovr.ecommerce.catalog.item.filters.Color;
import com.bekrenovr.ecommerce.catalog.item.metadata.ItemMetadata;
import com.bekrenovr.ecommerce.catalog.item.uniqueitem.UniqueItemResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public final class ItemDetailedResponse {
    private UUID id;
    private String name;
    private String description;
    private Double price;
    private Double discount;
    private Double priceAfterDiscount;
    private Color color;
    private String itemCode;
    private List<UniqueItemResponse> uniqueItems;
    private List<ItemResponse> similarItems;
    private String brand;
    private Double rating;
    private ItemMetadata metadata;
}
