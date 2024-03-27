package com.bekrenovr.ecommerce.catalog.service.filter;

import com.bekrenovr.ecommerce.catalog.dto.request.FilterOptionsModel;
import com.bekrenovr.ecommerce.catalog.model.entity.Item;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.math.DoubleRange;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ItemFilter {

    private List<Item> items;
    public List<Item> filter(FilterOptionsModel filterOptions){
        ItemFiltersProcessor processor = new ItemFiltersProcessor(this.items);

        if(this.items == null)
            throw new NullPointerException("Can't filter null list of items");

        if(filterOptions.priceRange() != null){
            DoubleRange priceRange =
                    new DoubleRange(filterOptions.priceRange().get(0), filterOptions.priceRange().get(1));
            processor.byPriceRange(priceRange);
        }

        if(filterOptions.sizes() != null)
            processor.bySizes(filterOptions.sizes());

        if(filterOptions.colors() != null)
            processor.byColors(filterOptions.colors());

        if(filterOptions.brandsIds() != null)
            processor.byBrands(filterOptions.brandsIds());

        if(filterOptions.season() != null)
            processor.bySeason(filterOptions.season());

        if(filterOptions.materials() != null)
            processor.byMaterials(filterOptions.materials());

        if(filterOptions.rating() != null)
            processor.byRating(filterOptions.rating());

        return processor.filter();
    }
}
