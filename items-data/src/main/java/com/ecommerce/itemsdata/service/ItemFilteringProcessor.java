package com.ecommerce.itemsdata.service;

import com.ecommerce.itemsdata.model.*;
import com.ecommerce.itemsdata.repository.ColorRepository;
import com.ecommerce.itemsdata.util.StringParser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.IntRange;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class ItemFilteringProcessor {

    public StreamFilteringProcessor forItems(List<Item> items){
        return new StreamFilteringProcessor(items);
    }

    public class StreamFilteringProcessor {

        private List<Item> items;
        private final ItemFilters itemFilters;

        public StreamFilteringProcessor(List<Item> items) {
            this.items = items;
            this.itemFilters = new ItemFilters(items);
        }

        public List<Item> withArgs (
                String priceRange,
                String sizes,
                String colors,
                String brands,
                Season season,
                String materials,
                Double rating
        ){
            if(items == null){
                throw new NullPointerException("Can't filter null list of items");
            }

            if(priceRange != null){
                DoubleRange parsedRange = StringParser.parseDoubleRange(priceRange);
                items = itemFilters.filterByPriceRange(parsedRange);
            }

            if(sizes != null){
                List<String> sizesList = StringParser.parseDelimitedList(sizes, "\\.");
                items = itemFilters.filterBySizes(sizesList);
            }

            if(colors != null){
                List<ColorEnum> colorsList = StringParser.parseColors(colors);
                items = itemFilters.filterByColors(colorsList);
            }

            if(brands != null){
                List<String> brandsList = StringParser.parseDelimitedList(brands, "\\.");
                items = itemFilters.filterByBrands(brandsList);
            }

            if(season != null){
                items = itemFilters.filterBySeason(season);
            }

            if(materials != null){
                List<Material> materialList = StringParser.parseMaterials(materials);
                items = itemFilters.filterByMaterials(materialList);
            }

            if(rating != null){
                items = itemFilters.filterByRating(rating);
            }

            return items;
        }
    }
}
