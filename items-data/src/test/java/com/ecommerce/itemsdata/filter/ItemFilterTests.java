package com.ecommerce.itemsdata.filter;

import com.ecommerce.itemsdata.ItemsDataApplication;
import com.ecommerce.itemsdata.dto.request.FilterOptionsModel;
import com.ecommerce.itemsdata.model.*;
import com.ecommerce.itemsdata.service.filter.ItemFilter;
import com.ecommerce.itemsdata.util.dev.ItemGenerator;
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

import static com.ecommerce.itemsdata.model.ColorEnum.*;
import static com.ecommerce.itemsdata.model.Material.*;
import static com.ecommerce.itemsdata.model.Season.multiseason;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ItemsDataApplication.class})
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
            List<ColorEnum> colors = Arrays.asList(black, blue, yellow);

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
            List<String> brands = Arrays.asList("gucci", "Balenciaga", "New balance");

            // act
            List<Item> filteredItems = itemFilter.filter(
                    new FilterOptionsModel(
                            null, null, null, brands, null, null, null
                    )
            );
            boolean success = allItemsMatchByBrandIgnoreCase(filteredItems, brands);

            // assert
            assertTrue(success);
        }

        @Test
        void filterItemsBySeason(){
            // arrange
            Season season = multiseason;

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
            List<Material> materials = Arrays.asList(wool, denim);

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
            List<ColorEnum> colors = Arrays.asList(white, black, yellow);

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
            List<ColorEnum> colors = Arrays.asList(green, violet);
            List<Material> materials = Arrays.asList(cotton, synthetics);

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
            List<String> itemSizes = item.getSizes()
                    .stream()
                    .map(Size::getValue)
                    .toList();
            return itemSizes.stream()
                    .anyMatch(sizeValues::contains);
        };
        return items.stream()
                .map(predicate::test)
                .reduce(true, (a,b) -> a && b);
    }

    private boolean allItemsMatchByColor(List<Item> items, List<ColorEnum> colors){
        Predicate<Item> predicate = item -> {
            List<ColorEnum> itemColors = item.getColors()
                    .stream()
                    .map(Color::getValue)
                    .toList();
            return itemColors.stream()
                    .anyMatch(colors::contains);
        };
        return items.stream()
                .map(predicate::test)
                .reduce(true, (a,b) -> a && b);
    }

    private boolean seasonMatchesForAllItems(List<Item> items, Season season){
        return items.stream()
                .map(item -> item.getSeason().equals(season))
                .reduce(true, (a,b) -> a && b);
    }

    private boolean allItemsMatchByBrandIgnoreCase(List<Item> items, List<String> brands){
        List<String> brandsLowercase = brands.stream()
                .map(String::toLowerCase)
                .toList();
        return items.stream()
                .map(item -> brandsLowercase.contains(item.getBrand().toLowerCase()))
                .reduce(true, (a,b) -> a && b);
    }

    private boolean allItemsMatchByMaterial(List<Item> items, List<Material> materials){
        return items.stream()
                .map(item -> materials.contains(item.getMaterial()))
                .reduce(true, (a,b) -> a && b);
    }

}

