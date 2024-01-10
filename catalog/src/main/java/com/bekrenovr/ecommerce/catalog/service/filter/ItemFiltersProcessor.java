package com.bekrenovr.ecommerce.catalog.service.filter;

import com.bekrenovr.ecommerce.catalog.model.*;
import org.apache.commons.lang.math.DoubleRange;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ItemFiltersProcessor {

    private List<Item> items;
    private final Supplier<Stream<Item>> itemsSupplier = () -> items.stream();

    public ItemFiltersProcessor(List<Item> items) {
        this.items = items;
    }

    public void byPriceRange(DoubleRange parsedRange) {
        items = itemsSupplier.get()
                .filter(item -> parsedRange.containsDouble(item.getPriceAfterDiscount()))
                .collect(Collectors.toList());
    }

    public void bySizes(List<String> sizes) {
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
    }

    public void byColors(List<ColorEnum> colors) {
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
    }

    public void byBrands(List<Long> brandsIds) {
        /*List<String> brandsLowercase = brands.stream()
                .map(String::toLowerCase)
                .toList();*/
        items = itemsSupplier.get()
                .filter(item -> brandsIds.contains(item.getBrand().getId()))
                .collect(Collectors.toList());
    }

    public void bySeason(Season season) {
        items = itemsSupplier.get()
                .filter(item -> item.getSeason().equals(season))
                .collect(Collectors.toList());
    }

    public void byMaterials(List<Material> materials) {
        items = itemsSupplier.get()
                .filter(item -> materials.contains(item.getMaterial()))
                .collect(Collectors.toList());
    }

    public void byRating(Short rating) {
        items = itemsSupplier.get()
                .filter(item -> item.getRating() >= rating)
                .collect(Collectors.toList());
    }

    public List<Item> filter(){
        return items;
    }
}
