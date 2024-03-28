package com.bekrenovr.ecommerce.catalog.dto.request;

import com.bekrenovr.ecommerce.catalog.model.entity.Brand;
import com.bekrenovr.ecommerce.catalog.model.enums.Color;
import com.bekrenovr.ecommerce.catalog.model.enums.Material;
import com.bekrenovr.ecommerce.catalog.model.enums.Season;

import java.util.Collection;
import java.util.List;

public record FilterOptions(
        List<Integer> priceRange,
        Collection<String> sizes,
        Collection<Color> colors,
        Collection<Brand> brands,
        Collection<Material> materials,
        Season season,
        Short rating
) {}
