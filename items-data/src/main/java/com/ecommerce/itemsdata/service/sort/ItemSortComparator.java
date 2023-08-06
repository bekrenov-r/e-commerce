package com.ecommerce.itemsdata.service.sort;

import com.ecommerce.itemsdata.model.Item;

import java.util.Comparator;

public class ItemSortComparator {

    private static final Comparator<Item> byNew = Comparator.comparing(
            item -> item.getItemDetails().getCreatedAt().toLocalDate(),
            Comparator.reverseOrder()
    );
    private static final Comparator<Item> byPriceAsc = Comparator.comparing(Item::getPriceAfterDiscount);
    private static final Comparator<Item> byPriceDesc = Comparator.comparing(
            Item::getPriceAfterDiscount,
            (a, b) -> Double.compare(b, a)
    );
    private static final Comparator<Item> byPopularity = Comparator.comparing(
            item -> item.getItemDetails().getOrdersCountLastMonth(),
            (a, b) -> Integer.compare(b, a)
    );

    public static Comparator<Item> forOption(SortOption option){
        if(option == null){
            return byPopularity;
        }
        return switch(option) {
            case BY_NEW -> byNew;
            case BY_PRICE_ASC -> byPriceAsc;
            case BY_PRICE_DESC -> byPriceDesc;
            case BY_POPULARITY -> byPopularity;
        };
    }
}