package com.bekrenovr.ecommerce.catalog.sort;

import com.bekrenovr.ecommerce.catalog.item.Item;
import com.bekrenovr.ecommerce.catalog.item.ItemRepository;
import com.bekrenovr.ecommerce.catalog.item.sorting.ItemSortComparators;
import com.bekrenovr.ecommerce.catalog.item.sorting.SortOption;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ItemSortTest {

    ItemRepository itemRepository;
    List<Item> data;

    @Autowired
    public ItemSortTest(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
        data = itemRepository.findAll();
    }

    @Test
    void sortByPopularity(){
        List<Item> sortedItems = data.stream()
                .sorted(ItemSortComparators.forOption(SortOption.BY_POPULARITY))
                .toList();
        boolean success = isSortedByPopularity(sortedItems);

        assertTrue(success);
    }

    @Test
    void sortByNew(){
        List<Item> sortedItems = data.stream()
                .sorted(ItemSortComparators.forOption(SortOption.BY_NEW))
                .toList();
        boolean success = isSortedByNew(sortedItems);

        assertTrue(success);
    }

    @Test
    void sortByPriceAsc(){
        List<Item> sortedItems = data.stream()
                .sorted(ItemSortComparators.forOption(SortOption.BY_PRICE_ASC))
                .toList();
        boolean success = isSortedByPriceAsc(sortedItems);

        assertTrue(success);
    }

    @Test
    void sortByPriceDesc(){
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
