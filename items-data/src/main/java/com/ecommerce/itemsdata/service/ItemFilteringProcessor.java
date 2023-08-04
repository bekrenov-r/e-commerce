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
            if(items == null) throw new NullPointerException("Can't filter null list of items");


            if(priceRange != null){
                DoubleRange parsedRange = StringParser.parseDoubleRange(priceRange);
                itemFilters.byPriceRange(parsedRange);
            }

            if(sizes != null){
                List<String> sizesList = StringParser.parseDelimitedList(sizes, "\\.");
                itemFilters.bySizes(sizesList);
            }

            if(colors != null){
                List<ColorEnum> colorsList = StringParser.parseColors(colors);
                itemFilters.byColors(colorsList);
            }

            if(brands != null){
                List<String> brandsList = StringParser.parseDelimitedList(brands, "\\.");
                itemFilters.byBrands(brandsList);
            }

            if(season != null){
                itemFilters.bySeason(season);
            }

            if(materials != null){
                List<Material> materialList = StringParser.parseMaterials(materials);
                itemFilters.byMaterials(materialList);
            }

            if(rating != null){
                itemFilters.byRating(rating);
            }

            return itemFilters.filter();
        }
    }
}
