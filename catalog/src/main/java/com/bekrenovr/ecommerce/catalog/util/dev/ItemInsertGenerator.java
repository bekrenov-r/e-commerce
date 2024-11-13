package com.bekrenovr.ecommerce.catalog.util.dev;

import com.bekrenovr.ecommerce.catalog.item.Item;
import com.bekrenovr.ecommerce.catalog.item.details.ItemDetails;
import com.bekrenovr.ecommerce.catalog.item.image.ItemImage;
import com.bekrenovr.ecommerce.catalog.item.uniqueitem.UniqueItem;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class ItemInsertGenerator {
    private final ItemGenerator itemGenerator;

    public void generateItemInserts(int quantity) throws IOException {
        File file = new File("catalog/src/main/resources/item_inserts.sql");
        FileUtils.writeStringToFile(file, "", StandardCharsets.UTF_8);
        List<Item> items = itemGenerator.generateMultipleWithDetails(quantity)
                .stream()
                .peek(item -> {
                    UUID id = UUID.randomUUID();
                    item.setId(id);
                    item.getItemDetails().setItemId(id);
                }).toList();
        this.createItemInsert(items, file);

        List<ItemDetails> itemDetails = items.stream()
                .map(Item::getItemDetails)
                .toList();
        this.createItemDetailsInsert(itemDetails, file);

        List<UniqueItem> uniqueItems = items.stream()
                .map(Item::getUniqueItems)
                .flatMap(Collection::stream)
                .toList();
        this.createUniqueItemInsert(uniqueItems, file);

        List<ItemImage> images = items.stream()
                .map(Item::getImages)
                .flatMap(Collection::stream)
                .toList();
        this.createImageInsert(images, file);
    }

    private void createItemInsert(List<Item> items, File file) throws IOException {
        StringBuilder insert = new StringBuilder("-- ITEM\n");
        insert.append("INSERT INTO item (id, name, description, price, discount, price_after_discount, category_id, subcategory_id, brand_id, color, gender, collection, material, season, rating, item_code) \nVALUES ");
        String values = items.stream()
                .map(this::createRecord)
                .collect(Collectors.joining(",\n\t\t"));
        insert.append(values);
        insert.append(";\n");
        FileUtils.writeStringToFile(file, insert.toString(), StandardCharsets.UTF_8, true);
    }

    private void createItemDetailsInsert(List<ItemDetails> itemDetails, File file) throws IOException {
        StringBuilder insert = new StringBuilder("\n\n-- ITEM DETAILS\n");
        insert.append("INSERT INTO item_details (item_id, orders_count_total, orders_count_last_month, created_at, creating_employee) \nVALUES ");
        String values = itemDetails.stream()
                .map(this::createRecord)
                .collect(Collectors.joining(",\n\t\t"));
        insert.append(values);
        insert.append(";\n");
        FileUtils.writeStringToFile(file, insert.toString(), StandardCharsets.UTF_8, true);
    }

    private void createUniqueItemInsert(List<UniqueItem> uniqueItems, File file) throws IOException {
        StringBuilder insert = new StringBuilder("\n\n-- UNIQUE ITEM\n");
        insert.append("INSERT INTO unique_item (id, item_id, size, quantity) \nVALUES ");
        String values = uniqueItems.stream()
                .map(this::createRecord)
                .collect(Collectors.joining(",\n\t\t"));
        insert.append(values);
        insert.append(";\n");
        FileUtils.writeStringToFile(file, insert.toString(), StandardCharsets.UTF_8, true);
    }

    private void createImageInsert(List<ItemImage> images, File file) throws IOException {
        StringBuilder insert = new StringBuilder("\n\n-- IMAGE\n");
        insert.append("INSERT INTO image (id, item_id, path) \nVALUES ");
        String values = images.stream()
                .map(this::createRecord)
                .collect(Collectors.joining(",\n\t\t"));
        insert.append(values);
        insert.append(";\n");
        FileUtils.writeStringToFile(file, insert.toString(), StandardCharsets.UTF_8, true);
    }


    private String createRecord(Item item) {
        String template = "('${id}', '${name}', '${description}', ${price}, ${discount}, ${priceAfterDiscount}, '${categoryId}', ${subcategoryId}, '${brandId}', '${color}', '${gender}', '${collection}', '${material}', '${season}', ${rating}, '${itemCode}')";
        String subcategoryId = item.getSubcategory() != null
                ? "'" + item.getSubcategory().getId().toString() + "'"
                : "null";
        Map<String, Object> values = new HashMap<>();
        values.put("id", item.getId());
        values.put("name", item.getName());
        values.put("description", item.getDescription());
        values.put("price", item.getPrice());
        values.put("discount", item.getDiscount());
        values.put("priceAfterDiscount", item.getPriceAfterDiscount());
        values.put("categoryId", item.getCategory().getId());
        values.put("subcategoryId", subcategoryId);
        values.put("brandId", item.getBrand().getId());
        values.put("color", item.getColor());
        values.put("gender", item.getGender());
        values.put("collection", item.getCollection());
        values.put("material", item.getMaterial());
        values.put("season", item.getSeason());
        values.put("rating", item.getRating());
        values.put("itemCode", item.getItemCode());

        return StringSubstitutor.replace(template, values, "${", "}");
    }

    private String createRecord(ItemDetails itemDetails) {
        String template = "('${itemId}', ${ordersCountTotal}, ${ordersCountLastMonth}, '${createdAt}', '${creatingEmployee}')";
        Map<String, Object> values = new HashMap<>();
        values.put("itemId", itemDetails.getItemId().toString());
        values.put("ordersCountTotal", itemDetails.getOrdersCountTotal());
        values.put("ordersCountLastMonth", itemDetails.getOrdersCountLastMonth());
        values.put("createdAt", itemDetails.getCreatedAt());
        values.put("creatingEmployee", itemDetails.getCreatingEmployeeUsername());

        return StringSubstitutor.replace(template, values, "${", "}");
    }

    private String createRecord(UniqueItem uniqueItem) {
        String template = "(${id}, '${itemId}', '${size}', ${quantity})";
        Map<String, Object> values = new HashMap<>();
        values.put("id", "gen_random_uuid()");
        values.put("itemId",uniqueItem.getItem().getId());
        values.put("size", uniqueItem.getSize());
        values.put("quantity", uniqueItem.getQuantity());

        return StringSubstitutor.replace(template, values, "${", "}");
    }

    private String createRecord(ItemImage image) {
        String template = "(${id}, '${itemId}', '${path}')";
        Map<String, Object> values = new HashMap<>();
        values.put("id", "gen_random_uuid()");
        values.put("itemId", image.getItem().getId());
        values.put("path", image.getPath());
        return StringSubstitutor.replace(template, values, "${", "}");
    }
}
