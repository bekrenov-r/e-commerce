package com.ecommerce.salesdataservice.filter;

import com.ecommerce.itemsdata.ItemsDataApplication;
import com.ecommerce.itemsdata.model.*;
import com.ecommerce.itemsdata.service.ItemFilteringProcessor;
import com.ecommerce.itemsdata.util.dev.ItemGenerator;
import org.apache.commons.lang.math.DoubleRange;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static com.ecommerce.itemsdata.model.ColorEnum.*;
import static com.ecommerce.itemsdata.model.Material.*;
import static com.ecommerce.itemsdata.model.Season.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ItemsDataApplication.class})
public class ItemFilteringProcessorTests {

    ItemFilteringProcessor itemFilteringProcessor;
    ItemGenerator itemGenerator;
    List<Item> data;

    @Autowired
    public ItemFilteringProcessorTests(ItemFilteringProcessor itemFilteringProcessor, ItemGenerator itemGenerator) {
        this.itemFilteringProcessor = itemFilteringProcessor;
        this.itemGenerator = itemGenerator;
    }

    @BeforeEach
    void generateData(){
        this.data = itemGenerator.generateMultiple(1000);
    }

    @Nested
    class WithOneParam {
        @Test
        void filterItemsByPriceRange(){
            String priceRangeUnparsed = "10.40";
            DoubleRange priceRange = new DoubleRange(10, 40);

            List<Item> filteredItems = itemFilteringProcessor
                    .forItems(data)
                    .withArgs(priceRangeUnparsed, null, null, null, null, null, null);
            boolean success = allItemsHavePriceWithinRange(filteredItems, priceRange);

            assertTrue(success);
        }

        @Test
        void filterItemsBySizes_itemsListNotEmpty(){
            String sizesUnparsed = "S.XS.2XL";
            List<String> sizesParsed = new ArrayList<>(Arrays.asList("S", "XS", "2XL"));

            List<Item> filteredItems = itemFilteringProcessor
                    .forItems(data)
                    .withArgs(null, sizesUnparsed, null, null, null, null, null);
            boolean success = allItemsMatchBySize(filteredItems, sizesParsed);

            assertTrue(success);
        }

        @Test
        void filterItemsByColors_ignoringCase(){
            String colorsUnparsedStr = "Black.blue.YELLOW";
            List<ColorEnum> colorsParsed = Arrays.asList(black, blue, yellow);

            List<Item> filteredItems = itemFilteringProcessor
                    .forItems(data)
                    .withArgs(null, null, colorsUnparsedStr, null, null, null, null);
            boolean success = allItemsMatchByColor(filteredItems, colorsParsed);
//            List<ColorEnum> filteredItemsColors = filteredItems.stream()
//                    .map(Item::getColors)
//                    .flatMap(Collection::stream)
//                    .map(Color::getValue)
//                    .distinct()
//                    .collect(Collectors.toList());
//            filteredItemsColors.retainAll(expected);

            assertTrue(success);
        }

        @Test
        void filterItemsByBrands_ignoringCase(){
            String brandsUnparsedStr = "gucci.Balenciaga.New balance";
            List<String> brandsParsed = Arrays.asList("gucci", "Balenciaga", "New balance");

            List<Item> filteredItems = itemFilteringProcessor
                    .forItems(data)
                    .withArgs(null, null, null, brandsUnparsedStr, null, null, null);
//            List<String> filteredItemsNames = filteredItems.stream().map(Item::getName).toList();
            boolean success = allItemsMatchByBrandIgnoreCase(filteredItems, brandsParsed);

            assertTrue(success);
        }

        @Test
        void filterItemsBySeason(){
            Season season = multiseason;

            List<Item> filteredItems = itemFilteringProcessor
                    .forItems(data)
                    .withArgs(null, null, null, null, season, null, null);
            boolean success = seasonMatchesForAllItems(filteredItems, season);

            assertTrue(success);
        }

        @Test
        void filterItemsByMaterials(){
            String materialsUnparsed = "wool.Denim";
            List<Material> materialsParsed = Arrays.asList(wool, denim);

            List<Item> filteredItems = itemFilteringProcessor
                    .forItems(data)
                    .withArgs(null, null, null, null, null, materialsUnparsed, null);
            boolean success = allItemsMatchByMaterial(filteredItems, materialsParsed);

            assertTrue(success);
        }
    }

    @Nested
    class WithMultipleParams {
        @Test
        void filterItemsBySizesAndColors(){
            String sizesUnparsed = "S.XS";
            String colorsUnparsed = "white.Black.YELLOW";
            List<String> sizesParsed = Arrays.asList("S", "XS");
            List<ColorEnum> colorsParsed = Arrays.asList(white, black, yellow);

            List<Item> filteredItems = itemFilteringProcessor
                    .forItems(data)
                    .withArgs(null, sizesUnparsed, colorsUnparsed, null, null, null, null);
            boolean successBySizes = allItemsMatchBySize(filteredItems, sizesParsed);
            boolean successByColors = allItemsMatchByColor(filteredItems, colorsParsed);

            assertAll(
                    () -> assertTrue(successBySizes),
                    () -> assertTrue(successByColors)
            );
        }

        @Test
        void filterItemsBySizesColorsAndMaterials(){
            String sizesUnparsed = "L.XL";
            List<String> sizesParsed = Arrays.asList("L", "XL");
            String colorsUnparsed = "Green.violet";
            List<ColorEnum> colorsParsed = Arrays.asList(green, violet);
            String materialsUnparsed = "Cotton.synthetics";
            List<Material> materialsParsed = Arrays.asList(cotton, synthetics);

            List<Item> filteredItems = itemFilteringProcessor
                    .forItems(data)
                    .withArgs(null, sizesUnparsed, colorsUnparsed, null, null, materialsUnparsed, null);
            boolean successBySizes = allItemsMatchBySize(filteredItems, sizesParsed);
            boolean successByColors = allItemsMatchByColor(filteredItems, colorsParsed);
            boolean successByMaterials = allItemsMatchByMaterial(filteredItems, materialsParsed);

            assertAll(
                    () -> assertTrue(successBySizes),
                    () -> assertTrue(successByColors),
                    () -> assertTrue(successByMaterials)
            );
        }
    }

    @Test
    void throwsNullPointerException_whenItemsListIsNull(){
        Executable exec = () -> itemFilteringProcessor
                .forItems(null)
                .withArgs(null, null, null, null, null, null, null);

        assertThrows(NullPointerException.class, exec);
    }

    private boolean allItemsHavePriceWithinRange(List<Item> items, DoubleRange range){
        return items.stream()
                .map(item -> range.containsDouble(item.getPriceAfterDiscount()))
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
