package com.bekrenovr.ecommerce.catalog.item.filters;

import com.bekrenovr.ecommerce.catalog.item.size.Size;
import org.apache.commons.lang.math.DoubleRange;

import java.util.Collection;
import java.util.UUID;

public record FilterOptions(
        Gender gender,
        UUID categoryId,
        UUID subcategoryId,
        DoubleRange priceRange,
        Collection<Size> sizes,
        Collection<Color> colors,
        Collection<UUID> brandsIds,
        Collection<Material> materials,
        Season season,
        Short rating,
        @jakarta.validation.constraints.Size(min = 3)
        String searchPattern
) {}
