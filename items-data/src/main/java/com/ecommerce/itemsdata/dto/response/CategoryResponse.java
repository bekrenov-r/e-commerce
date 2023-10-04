package com.ecommerce.itemsdata.dto.response;

import java.util.List;

public record CategoryResponse(
        Long id,
        String name,
        List<SubcategoryResponse> subcategories
) {
}
