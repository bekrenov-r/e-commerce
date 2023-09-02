package com.ecommerce.itemsdata.datasets;

import com.ecommerce.itemsdata.model.Item;
import com.ecommerce.itemsdata.util.dev.ItemGenerator;

import java.util.ArrayList;
import java.util.List;

public class Items {

    public static List<Item> randomItemsWithDetails(int count){
        return new ItemGenerator().generateMultipleWithDetails(count);
    }

    public static List<Item> itemsForPopularityTests(int count){
        List<Item> res = new ArrayList<>();
        for(long i = 1; i <= count; i++){
            res.add(
                    Item.builder()
                            .id(i)
                            .itemDetails(ItemsDetails.theItemDetails()
                                    .itemId(i)
                                    .ordersCountLastMonth((int) i)
                                    .build()
                            )
                            .build()
            );
        }
        return res;
    }
}
