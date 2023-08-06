package com.ecommerce.salesdataservice.sort;

import com.ecommerce.itemsdata.ItemsDataApplication;
import com.ecommerce.itemsdata.model.Item;
import com.ecommerce.itemsdata.service.filter.ItemFilteringProcessor;
import com.ecommerce.itemsdata.service.sort.ItemSortComparator;
import com.ecommerce.itemsdata.service.sort.SortOption;
import com.ecommerce.itemsdata.util.dev.ItemGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ItemsDataApplication.class})
public class ItemSortTests {

    ItemGenerator itemGenerator;
    List<Item> data;

    @Autowired
    public ItemSortTests(ItemGenerator itemGenerator) {
        this.itemGenerator = itemGenerator;
    }

    @BeforeEach
    void generateData(){
        this.data = itemGenerator.generateMultipleWithDetails(1000);
    }

    @Test
    void sortByPopularityTest(){
        List<Item> sortedItems = data.stream()
                .sorted(ItemSortComparator.forOption(SortOption.BY_POPULARITY))
                .toList();
        boolean success = isSortedByPopularity(sortedItems);

        assertTrue(success);
    }

    @Test
    void sortByNewTest(){
        List<Item> sortedItems = data.stream()
                .sorted(ItemSortComparator.forOption(SortOption.BY_NEW))
                .toList();
        boolean success = isSortedByNew(sortedItems);

        assertTrue(success);
    }

    @Test
    void sortByPriceAscTest(){
        List<Item> sortedItems = data.stream()
                .sorted(ItemSortComparator.forOption(SortOption.BY_PRICE_ASC))
                .toList();
        boolean success = isSortedByPriceAsc(sortedItems);

        assertTrue(success);
    }

    @Test
    void sortByPriceDescTest(){
        List<Item> sortedItems = data.stream()
                .sorted(ItemSortComparator.forOption(SortOption.BY_PRICE_DESC))
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
