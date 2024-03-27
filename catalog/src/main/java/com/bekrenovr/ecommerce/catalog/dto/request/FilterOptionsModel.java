package com.bekrenovr.ecommerce.catalog.dto.request;

import com.bekrenovr.ecommerce.catalog.model.enums.Color;
import com.bekrenovr.ecommerce.catalog.model.enums.Material;
import com.bekrenovr.ecommerce.catalog.model.enums.Season;

import java.util.List;

public record FilterOptionsModel(
        List<Integer> priceRange,
        List<String> sizes,
        List<Color> colors,
        List<Long> brandsIds,
        List<Material> materials,
        Season season,
        Short rating
) {}
