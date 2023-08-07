package com.ecommerce.itemsdata.service.sort;

import com.ecommerce.itemsdata.model.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Comparator;

import static com.ecommerce.itemsdata.service.sort.ItemSortComparators.*;

@AllArgsConstructor
@Getter
public enum SortOption {

    BY_PRICE_ASC("price_asc", byPriceAsc),
    BY_PRICE_DESC("price_desc", byPriceDesc),
    BY_NEW("new", byNew),
    BY_POPULARITY("popularity", byPopularity);

    private final String string;
    private final Comparator<Item> comparator;

    public static SortOption ofString(String s){
        return switch(s){
            case "price_asc" -> BY_PRICE_ASC;
            case "price_desc" -> BY_PRICE_DESC;
            case "new" -> BY_NEW;
            case "popularity" -> BY_POPULARITY;
            default -> BY_POPULARITY;
        };
    }

}
