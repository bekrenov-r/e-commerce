package com.ecommerce.itemsdata.dto.request;

import com.ecommerce.itemsdata.model.ColorEnum;
import com.ecommerce.itemsdata.model.Material;
import com.ecommerce.itemsdata.model.Season;

import java.util.List;

public record FilterOptionsModel(
        List<Integer> priceRange,
        List<String> sizes,
        List<ColorEnum> colors,
        List<String> brands,
        List<Material> materials,
        Season season,
        Short rating
) {}
