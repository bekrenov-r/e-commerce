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
public class ItemResponse {
    private UUID id;
    private String name;
    private Double price;
    private Double discount;
    private Double priceAfterDiscount;
    private String brand;
    private Double rating;
    private Color color;
    private List<UniqueItemResponse> uniqueItems;
    private ItemMetadata metadata;
}
