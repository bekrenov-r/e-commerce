package com.bekrenovr.ecommerce.catalog.dto.response;

import com.bekrenovr.ecommerce.catalog.model.enums.Color;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private List<UniqueItemShortResponse> uniqueItems;
    private List<ReviewResponse> reviews;
    private List<ItemResponse> similarItems;
    @JsonProperty("isOnWishList")
    private boolean isOnWishList;
    @JsonProperty("isAvailable")
    private boolean isAvailable;
    @JsonProperty("isNew")
    private boolean isNew;
    @JsonProperty("isPopular")
    private boolean isPopular;
    private String brand;
    private Double rating;
}
