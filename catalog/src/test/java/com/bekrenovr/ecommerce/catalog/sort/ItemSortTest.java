package com.bekrenovr.ecommerce.catalog.sort;

import com.bekrenovr.ecommerce.catalog.CatalogApplication;
import com.bekrenovr.ecommerce.catalog.model.entity.Item;
import com.bekrenovr.ecommerce.catalog.service.sort.ItemSortComparators;
import com.bekrenovr.ecommerce.catalog.service.sort.SortOption;
import com.bekrenovr.ecommerce.catalog.util.dev.ItemGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {CatalogApplication.class})
@ActiveProfiles("test")
public class ItemSortTest {

    ItemGenerator itemGenerator;
    List<Item> data;

    @Autowired
    public ItemSortTest(ItemGenerator itemGenerator) {
        this.itemGenerator = itemGenerator;
    }

    @BeforeEach
    void generateData(){
        this.data = itemGenerator.generateMultipleWithDetails(1000);
    }

    @Test
    void sortByPopularityTest(){
        List<Item> sortedItems = data.stream()
                .sorted(ItemSortComparators.forOption(SortOption.BY_POPULARITY))
                .toList();
        boolean success = isSortedByPopularity(sortedItems);

        assertTrue(success);
    }

    @Test
    void sortByNewTest(){
        List<Item> sortedItems = data.stream()
                .sorted(ItemSortComparators.forOption(SortOption.BY_NEW))
                .toList();
        boolean success = isSortedByNew(sortedItems);

        assertTrue(success);
    }

    @Test
    void sortByPriceAscTest(){
        List<Item> sortedItems = data.stream()
                .sorted(ItemSortComparators.forOption(SortOption.BY_PRICE_ASC))
                .toList();
        boolean success = isSortedByPriceAsc(sortedItems);

        assertTrue(success);
    }

    @Test
    void sortByPriceDescTest(){
        List<Item> sortedItems = data.stream()
                .sorted(ItemSortComparators.forOption(SortOption.BY_PRICE_DESC))
                .toList();
        boolean success = isSortedByPriceDesc(sortedItems);

        assertTrue(success);
    }

    private boolean isSortedByPopularity(List<Item> items){
        for(int i = 0; i < items.size() - 1; i++){
            if(items.get(i).getItemDetails().getOrdersCountLastMonth()
                    < items.get(i+1).getItemDetails().getOrdersCountLastMonth()){
                return false;
            }
        }
        return true;
    }

    private boolean isSortedByNew(List<Item> items){
        List<Long> createdAtDaysOfEpoch = items.stream()
                .map(item -> item.getItemDetails().getCreatedAt().toLocalDate().toEpochDay())
                .toList();
        for(int i = 0; i < items.size() - 1; i++){
            if(createdAtDaysOfEpoch.get(i) < createdAtDaysOfEpoch.get(i+1)){
                return false;
            }
        }
        return true;
    }

    private boolean isSortedByPriceAsc(List<Item> items){
        for(int i = 0; i < items.size() - 1; i++){
            if(items.get(i).getPriceAfterDiscount() > items.get(i+1).getPriceAfterDiscount()){
                return false;
            }
        }
        return true;
    }

    private boolean isSortedByPriceDesc(List<Item> items){
        for(int i = 0; i < items.size() - 1; i++){
            if(items.get(i).getPriceAfterDiscount() < items.get(i+1).getPriceAfterDiscount()){
                return false;
            }
        }
        return true;
    }

}
