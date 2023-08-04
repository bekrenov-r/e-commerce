package com.ecommerce.itemsdata.service;

import com.ecommerce.itemsdata.model.*;
import org.apache.commons.lang.math.DoubleRange;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ItemFilters {

    private List<Item> items;
    private final Supplier<Stream<Item>> itemsSupplier = () -> items.stream();

    public ItemFilters(List<Item> items) {
        this.items = items;
    }

    public List<Item> filterByPriceRange(DoubleRange parsedRange) {
        items = itemsSupplier.get()
                .filter(item -> parsedRange.containsDouble(item.getPriceAfterDiscount()))
                .collect(Collectors.toList());
        return items;
    }

    public List<Item> filterBySizes(List<String> sizes) {
        List<String> sizesLowercase = sizes.stream()
                .map(String::toLowerCase)
                .toList();
        Predicate<Item> predicate = item -> {
            List<String> itemSizesLowercase = item.getSizes()
                    .stream()
                    .distinct()
                    .map(size -> size.getValue().toLowerCase())
                    .toList();
            return itemSizesLowercase.stream()
                    .anyMatch(sizesLowercase::contains);
        };
        items = itemsSupplier.get()
                .filter(predicate)
                .collect(Collectors.toList());
        return items;
    }

    public List<Item> filterByColors(List<ColorEnum> colors) {
        Predicate<Item> predicate = item -> {
            List<ColorEnum> itemColorEnumValues = item.getColors()
                    .stream()
                    .map(Color::getValue)
                    .toList();
            return itemColorEnumValues.stream()
                    .anyMatch(colors::contains);
        };
        items = itemsSupplier.get()
                .filter(predicate)
                .collect(Collectors.toList());
        return items;
    }

    public List<Item> filterByBrands(List<String> brands) {
        List<String> brandsLowercase = brands.stream()
                .map(String::toLowerCase)
                .toList();
        items = itemsSupplier.get()
                .filter(item -> brandsLowercase.contains(item.getBrand().toLowerCase()))
                .collect(Collectors.toList());
        return items;
    }

    public List<Item> filterBySeason(Season season) {
        items = itemsSupplier.get()
                .filter(item -> item.getSeason().equals(season))
                .collect(Collectors.toList());
        return items;
    }

    public List<Item> filterByMaterials(List<Material> materials) {
        items = itemsSupplier.get()
                .filter(item -> materials.contains(item.getMaterial()))
                .collect(Collectors.toList());
        return items;
    }

    public List<Item> filterByRating(Double rating) {
        items = itemsSupplier.get()
                .filter(item -> item.getRating() >= rating)
                .collect(Collectors.toList());
        return items;
    }
}
