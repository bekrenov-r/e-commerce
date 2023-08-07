package com.ecommerce.itemsdata.service.sort;

import com.ecommerce.itemsdata.model.Item;

import java.util.Comparator;

public class ItemSortComparators {

    public static final Comparator<Item> byNew = Comparator.comparing(
            item -> item.getItemDetails().getCreatedAt().toLocalDate(),
            Comparator.reverseOrder()
    );
    public static final Comparator<Item> byPriceAsc = Comparator.comparing(Item::getPriceAfterDiscount);
    public static final Comparator<Item> byPriceDesc = Comparator.comparing(
            Item::getPriceAfterDiscount,
            (a, b) -> Double.compare(b, a)
    );
    public static final Comparator<Item> byPopularity = Comparator.comparing(
            item -> item.getItemDetails().getOrdersCountLastMonth(),
            (a, b) -> Integer.compare(b, a)
    );
}