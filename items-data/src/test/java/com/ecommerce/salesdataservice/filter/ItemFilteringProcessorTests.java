package com.ecommerce.salesdataservice.filter;

import com.ecommerce.itemsdata.ItemsDataApplication;
import com.ecommerce.itemsdata.model.*;
import com.ecommerce.itemsdata.service.ItemFilteringProcessor;
import com.ecommerce.itemsdata.util.dev.ItemGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.ecommerce.itemsdata.model.ColorEnum.*;
import static com.ecommerce.itemsdata.model.Season.*;
import static java.util.Arrays.stream;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Test
//    @RepeatedTest(10)
    void filterItemsBySizes_itemsListNotEmpty(){
        String sizeValues = "S.XS.2XL";
        List<String> expected = new ArrayList<>(Arrays.asList("S", "XS", "2XL"));

        List<Item> filteredItems = itemFilteringProcessor
                .forItems(data)
                .withArgs(null, sizeValues, null, null, null, null, null);
        List<String> filteredItemsSizes = filteredItems.stream()
                .map(Item::getSizes)
                .flatMap(Collection::stream)
                .map(Size::getValue)
                .distinct()
                .collect(Collectors.toList());
        System.out.println(filteredItemsSizes);
        expected.retainAll(filteredItemsSizes);

        assertFalse(expected.isEmpty());
//        assertIterableEquals(Arrays.asList("Item1", "Item3"), filteredItemsNames);
    }

    @Test
    void filterItemsByColors_ignoringCase(){
        String colorsUnparsedStr = "Black.blue.YELLOW";
        List<ColorEnum> expected = Arrays.asList(black, blue, yellow);

        List<Item> filteredItems = itemFilteringProcessor
                .forItems(data)
                .withArgs(null, null, colorsUnparsedStr, null, null, null, null);
        List<ColorEnum> filteredItemsColors = filteredItems.stream()
                .map(Item::getColors)
                .flatMap(Collection::stream)
                .map(Color::getValue)
                .distinct()
                .collect(Collectors.toList());
        filteredItemsColors.retainAll(expected);

        assertFalse(filteredItemsColors.isEmpty());
    }

    @Test
    void filterItemsByBrands_ignoringCase(){
        List<Item> items = Arrays.asList(
                Item.builder().brand("Gucci").name("Item1").build(),
                Item.builder().brand("Balenciaga").name("Item2").build(),
                Item.builder().brand("Adidas").name("Item3").build(),
                Item.builder().brand("Puma").name("Item4").build(),
                Item.builder().brand("New Balance").name("Item5").build()
        );
        String brandsUnparsedStr = "gucci.Balenciaga.New balance";

        List<Item> filteredItems = itemFilteringProcessor
                .forItems(items)
                .withArgs(null, null, null, brandsUnparsedStr, null, null, null);
        List<String> filteredItemsNames = filteredItems.stream().map(Item::getName).toList();

        assertIterableEquals(Arrays.asList("Item1", "Item2", "Item5"), filteredItemsNames);
    }

    @Test
    void filterItemsBySeason(){
        Season season = multiseason;

        List<Item> filteredItems = itemFilteringProcessor
                .forItems(data)
                .withArgs(null, null, null, null, season, null, null);
        boolean allItemsHaveRightSeason = filteredItems.stream()
                .map(item -> item.getSeason().equals(season))
                .reduce(true, (a,b) -> a && b, (a, b) -> a && b);

        assertTrue(allItemsHaveRightSeason);
    }

    @Test
    void filterItemsBySizesAndColors(){
        String sizesUnparsed = "S.XS";
        String colorsUnparsed = "white.Black.YELLOW";
        List<String> sizes = Arrays.asList("S", "XS");
        List<ColorEnum> colors = Arrays.asList(white, black, yellow);

        List<Item> filteredItems = itemFilteringProcessor
                .forItems(data)
                .withArgs(null, sizesUnparsed, colorsUnparsed, null, null, null, null);
        boolean allItemsFitBySize = allItemsFitBySize(filteredItems, sizes);
        boolean allItemsFitByColor = allItemsFitByColor(filteredItems, colors);

        assertAll(
                () -> assertTrue(allItemsFitBySize),
                () -> assertTrue(allItemsFitByColor)
        );
    }

    @Test
    void filterItemsBySizesColorsAndMaterials(){
        List<Item> data = itemGenerator.generateMultiple(100);

    }

    private boolean allItemsFitBySize(List<Item> items, List<String> sizeValues){
        Predicate<Item> predicate = item -> {
            List<String> itemSizes = item.getSizes()
                    .stream()
                    .map(Size::getValue)
                    .toList();
            return itemSizes.stream()
                    .anyMatch(sizeValues::contains);
        };
        return !items.stream()
                .filter(predicate)
                .toList()
                .isEmpty();
    }

    private boolean allItemsFitByColor(List<Item> items, List<ColorEnum> colors){
        Predicate<Item> predicate = item -> {
            List<ColorEnum> itemColors = item.getColors()
                    .stream()
                    .map(Color::getValue)
                    .toList();
            return itemColors.stream()
                    .anyMatch(colors::contains);
        };
        return !items.stream()
                .filter(predicate)
                .toList()
                .isEmpty();
    }

}
