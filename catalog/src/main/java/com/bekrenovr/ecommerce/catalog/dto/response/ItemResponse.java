package com.bekrenovr.ecommerce.catalog.dto.response;

import com.bekrenovr.ecommerce.catalog.model.enums.Color;
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
    private List<UniqueItemShortResponse> uniqueItems;
    private ItemMetadata metadata;
}
