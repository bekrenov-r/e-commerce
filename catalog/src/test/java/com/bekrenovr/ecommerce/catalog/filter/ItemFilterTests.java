package com.bekrenovr.ecommerce.catalog.filter;

import com.bekrenovr.ecommerce.catalog.CatalogApplication;
import com.bekrenovr.ecommerce.catalog.dto.request.FilterOptionsModel;
import com.bekrenovr.ecommerce.catalog.model.entity.Item;
import com.bekrenovr.ecommerce.catalog.model.entity.UniqueItem;
import com.bekrenovr.ecommerce.catalog.model.enums.Color;
import com.bekrenovr.ecommerce.catalog.model.enums.Material;
import com.bekrenovr.ecommerce.catalog.model.enums.Season;
import com.bekrenovr.ecommerce.catalog.service.filter.ItemFilter;
import com.bekrenovr.ecommerce.catalog.util.dev.ItemGenerator;
import org.apache.commons.lang.math.DoubleRange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static com.bekrenovr.ecommerce.catalog.model.enums.Color.*;
import static com.bekrenovr.ecommerce.catalog.model.enums.Material.*;
import static com.bekrenovr.ecommerce.catalog.model.enums.Season.MULTISEASON;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {CatalogApplication.class})
public class ItemFilterTests {
    ItemGenerator itemGenerator;
    List<Item> data;

    ItemFilter itemFilter;

    @Autowired
    public ItemFilterTests(ItemGenerator itemGenerator) {
        this.itemGenerator = itemGenerator;
    }

    @BeforeEach
    void generateData(){
        this.data = itemGenerator.generateMultiple(1000);
        itemFilter = new ItemFilter(data);
    }

    @Nested
    class WithOneParam {
        @Test
        void filterItemsByPriceRange(){
            // arrange
            List<Integer> range = Arrays.asList(10, 40);

            // act
            List<Item> filteredItems = itemFilter.filter(
                    new FilterOptionsModel(
                            range, null, null, null, null, null, null
                    )
            );
            boolean success = allItemsHavePriceWithinRange(filteredItems, range);

            // assert
            assertTrue(success);
        }

        @Test
        void filterItemsBySizes_itemsListNotEmpty(){
            // arrange
            List<String> sizes = new ArrayList<>(Arrays.asList("S", "XS", "2XL"));

            // act
            List<Item> filteredItems = itemFilter.filter(
                    new FilterOptionsModel(
                            null, sizes, null, null, null, null, null
                    )
            );
            boolean success = allItemsMatchBySize(filteredItems, sizes);

            // assert
            assertTrue(success);
        }

        @Test
        void filterItemsByColors_ignoringCase(){
            // arrange
            List<Color> colors = Arrays.asList(BLACK, BLUE, YELLOW);

            // act
            List<Item> filteredItems = itemFilter.filter(
                    new FilterOptionsModel(
                            null, null, colors, null, null, null, null
                    )
            );
            boolean success = allItemsMatchByColor(filteredItems, colors);

            // assert
            assertTrue(success);
        }

        @Test
        void filterItemsByBrands_ignoringCase(){
            // arrange
            List<Long> brandsIds = Arrays.asList(2L, 7L, 10L);

            // act
            List<Item> filteredItems = itemFilter.filter(
                    new FilterOptionsModel(
                            null, null, null, brandsIds, null, null, null
                    )
            );
            boolean success = allItemsMatchByBrandIgnoreCase(filteredItems, brandsIds);

            // assert
            assertTrue(success);
        }

        @Test
        void filterItemsBySeason(){
            // arrange
            Season season = MULTISEASON;

            // act
            List<Item> filteredItems = itemFilter.filter(
                    new FilterOptionsModel(
                            null, null, null, null, null, season, null
                    )
            );
            boolean success = seasonMatchesForAllItems(filteredItems, season);

            // assert
            assertTrue(success);
        }

        @Test
        void filterItemsByMaterials(){
            // arrange
            List<Material> materials = Arrays.asList(WOOL, DENIM);

            // act
            List<Item> filteredItems = itemFilter.filter(
                    new FilterOptionsModel(
                            null, null, null, null, materials, null, null
                    )
            );
            boolean success = allItemsMatchByMaterial(filteredItems, materials);

            // assert
            assertTrue(success);
        }
    }

    @Nested
    class WithMultipleParams {
        @Test
        void testFilterItemsBySizesAndColors(){
            // arrange
            List<String> sizes = Arrays.asList("S", "XS");
            List<Color> colors = Arrays.asList(WHITE, BLACK, YELLOW);

            // act
            List<Item> filteredItems = itemFilter.filter(
                    new FilterOptionsModel(
                            null, sizes, colors, null, null, null, null
                    )
            );
            boolean successBySizes = allItemsMatchBySize(filteredItems, sizes);
            boolean successByColors = allItemsMatchByColor(filteredItems, colors);

            // assert
            assertAll(
                    () -> assertTrue(successBySizes),
                    () -> assertTrue(successByColors)
            );
        }

        @Test
        void filterItemsBySizesColorsAndMaterials(){
            List<String> sizes = Arrays.asList("L", "XL");
            List<Color> colors = Arrays.asList(GREEN, VIOLET);
            List<Material> materials = Arrays.asList(COTTON, SYNTHETICS);

            List<Item> filteredItems = itemFilter.filter(
                    new FilterOptionsModel(
                            null, sizes, colors, null, materials, null, null
                    )
            );
            boolean successBySizes = allItemsMatchBySize(filteredItems, sizes);
            boolean successByColors = allItemsMatchByColor(filteredItems, colors);
            boolean successByMaterials = allItemsMatchByMaterial(filteredItems, materials);

            assertAll(
                    () -> assertTrue(successBySizes),
                    () -> assertTrue(successByColors),
                    () -> assertTrue(successByMaterials)
            );
        }
    }

    @Test
    void throwsNullPointerException_whenItemsListIsNull(){
        // arrange
        ItemFilter processor = new ItemFilter(null);

        Executable exec = () -> processor.filter(null);

        assertThrows(NullPointerException.class, exec);
    }

    private boolean allItemsHavePriceWithinRange(List<Item> items, List<Integer> range){
        DoubleRange doubleRange = new DoubleRange(range.get(0), range.get(1));
        return items.stream()
                .map(item -> doubleRange.containsDouble(item.getPriceAfterDiscount()))
                .reduce(true, (a,b) -> a && b);
    }

    private boolean allItemsMatchBySize(List<Item> items, List<String> sizeValues){
        Predicate<Item> predicate = item -> {
            List<String> itemSizes = item.getUniqueItems()
                    .stream()
                    .map(UniqueItem::getSize)
                    .toList();
            return itemSizes.stream()
                    .anyMatch(sizeValues::contains);
        };
        return items.stream()
                .map(predicate::test)
                .reduce(true, (a,b) -> a && b);
    }

    private boolean allItemsMatchByColor(List<Item> items, List<Color> colors){
        return items.stream()
                .allMatch(item -> colors.contains(item.getColor()));
    }

    private boolean seasonMatchesForAllItems(List<Item> items, Season season){
        return items.stream()
                .map(item -> item.getSeason().equals(season))
                .reduce(true, (a,b) -> a && b);
    }

    private boolean allItemsMatchByBrandIgnoreCase(List<Item> items, List<Long> brandsIds){
        return items.stream()
                .map(item -> brandsIds.contains(item.getBrand().getId()))
                .reduce(true, (a,b) -> a && b);
    }

    private boolean allItemsMatchByMaterial(List<Item> items, List<Material> materials){
        return items.stream()
                .map(item -> materials.contains(item.getMaterial()))
                .reduce(true, (a,b) -> a && b);
    }

}

