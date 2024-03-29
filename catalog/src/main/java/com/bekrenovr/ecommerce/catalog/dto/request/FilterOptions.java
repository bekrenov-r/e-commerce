package com.bekrenovr.ecommerce.catalog.dto.request;

import com.bekrenovr.ecommerce.catalog.model.Size;
import com.bekrenovr.ecommerce.catalog.model.entity.Brand;
import com.bekrenovr.ecommerce.catalog.model.enums.Color;
import com.bekrenovr.ecommerce.catalog.model.enums.Material;
import com.bekrenovr.ecommerce.catalog.model.enums.Season;
import org.apache.commons.lang.math.DoubleRange;

import java.util.Collection;

public record FilterOptions(
        DoubleRange priceRange,
        Collection<Size> sizes,
        Collection<Color> colors,
        Collection<Brand> brands,
        Collection<Material> materials,
        Season season,
        Short rating,
        @jakarta.validation.constraints.Size(min = 3)
        String searchPattern
) {}
