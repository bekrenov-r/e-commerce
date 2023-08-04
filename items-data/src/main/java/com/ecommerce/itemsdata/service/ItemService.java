package com.ecommerce.itemsdata.service;

import com.ecommerce.itemsdata.dto.response.ItemResponse;
import com.ecommerce.itemsdata.dto.mapper.ItemToDtoMapper;
import com.ecommerce.itemsdata.model.*;
import com.ecommerce.itemsdata.repository.CategoryRepository;
import com.ecommerce.itemsdata.repository.ItemDetailsRepository;
import com.ecommerce.itemsdata.repository.ItemRepository;
import com.ecommerce.itemsdata.util.StringParser;
import com.ecommerce.itemsdata.util.dev.ItemGenerator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.math.IntRange;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemDetailsRepository itemDetailsRepository;
    private final ItemGenerator itemGenerator;
    private final ItemToDtoMapper itemToDtoMapper;
    private final ItemFilteringProcessor itemFilteringProcessor;
    private final CategoryRepository categoryRepository;

    public List<ItemResponse> getAllItemsByGenderAndCategoryWithFilters(
            Gender gender, Long categoryId, String sort, String priceRange, String sizes, String colors, String brands, Season season, String materials, Double rating
    ) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("No category found for id: " + categoryId));

        List<Item> filteredItems = itemFilteringProcessor
                .forItems(itemRepository.findAllByGenderAndCategory(gender, category))
                .withArgs(priceRange, sizes, colors, brands, season, materials, rating);

        System.out.println(
                filteredItems.stream()
                        .map(Item::getSizes)
                        .flatMap(Collection::stream)
                        .map(Size::getValue)
                        .distinct()
                        .collect(Collectors.toList())
        );

        return filteredItems.stream()
                .map(itemToDtoMapper::itemToResponse)
                .toList();
    }

    public Item getItemById(Long id) {
        return itemRepository.findById(id).orElse(null);
    }

    // method for dev purpose
    public void createSampleItems(int quantity) {
        for(int i = 0; i < quantity; i++){
            Item item = itemGenerator.generate();
            createItem(item);
        }
    }

    public ResponseEntity<Void> createItem(Item item) {
        item.setId(null);
        Item savedItem = itemRepository.save(item);
        ItemResponse itemDTO = itemToDtoMapper.itemToResponse(savedItem);

        // todo: get creatingEmployeeId from security context
        ItemDetails itemDetails = new ItemDetails(0, 0, LocalDateTime.now(), "asd");
        itemDetails.setItem(savedItem);
        itemDetailsRepository.save(itemDetails);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedItem.getId())
                .toUri();
        return ResponseEntity.created(location).body(null);
    }



    /*private Item createItemWithDetails(Item item) {

        return transactionTemplate.execute(status -> {
            ItemDetails details = item.getItemDetails();
            item.setItemDetails(null);
            Item savedItem = itemRepository.save(item);

            details.setItem(savedItem);
            ItemDetails savedDetails = itemDetailsRepository.save(details);

            savedItem.setItemDetails(savedDetails);
            return savedItem;
        });
    }*/

    /*public ResponseEntity<List<ItemResponse>> findAll() {
        List<ItemResponse> itemResponses =
                itemRepository.findAll().stream().map(ItemToDtoMapper::itemToResponse).toList();
        return ResponseEntity
                .ok()
                .header("Response Source", responseSource.toString())
                .body(itemResponses);
    }

    public ResponseEntity<ItemResponse> findById(int id){
        *//*if(id <= 0){
            throw new IllegalArgumentException("Id must be greater than 0, provided: " + id);
        }*//*

        Item item =  itemRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No item found for id: " + id));

        ItemResponse itemResponse = ItemToDtoMapper.itemToResponse(item);

        *//*BigDecimal price = BigDecimal.valueOf(item.getPrice());
        BigDecimal discount = BigDecimal.valueOf(item.getDiscount());
        BigDecimal priceAfterDiscount =
                price.subtract(price.multiply(discount)).round(new MathContext(2));

        ItemResponse itemResponse = new ItemResponse(
                item.getId(),
                item.getPrice(),
                priceAfterDiscount.doubleValue(),
                item.getProducer(),
                item.getDescription()
        );*//*

        return ResponseEntity
                .ok()
                .header("Response Source", responseSource.toString())
                .body(itemResponse);
    }

    public ResponseEntity<List<ItemResponse>> findAllById(List<Integer> ids) {
        List<Item> items = itemRepository.findAllById(ids);

        if(items.size() != ids.size()){
            *//*List<Integer> foundIds = items.stream().map(Item::getId).toList();
            List<Integer> notFoundIds = ids.stream().filter(id -> !foundIds.contains(id)).toList();*//*
            List<Integer> notFoundIds = (List<Integer>) CollectionUtils.subtract(ids, items.stream().map(Item::getId).toList());
            throw new EntityNotFoundException("No items found for ids: " + notFoundIds);
        }

        List<ItemResponse> itemResponses = items
                .stream()
                .map(ItemToDtoMapper::itemToResponse)
                .toList();

        return ResponseEntity
                .ok(itemResponses);
    }

    public ResponseEntity<Item> create(Item item){
        item.setId(0);
        Item savedItem = itemRepository.save(item);

        return ResponseEntity
                .created(
                        ServletUriComponentsBuilder.fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(savedItem.getId())
                                .toUri()
                )
                .header("Response Source", responseSource.toString())
                .body(savedItem);
    }

    public ResponseEntity<Item> update(Item item){

        if(itemRepository.existsById(item.getId())){
            Item updatedItem = itemRepository.save(item);
            return ResponseEntity
                    .ok()
                    .header("Response Source", responseSource.toString())
                    .body(updatedItem);

        } else {
            throw new EntityNotFoundException("No item found for id: " + item.getId());
        }
    }

    public ResponseEntity<Void> delete(int id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No item found for id: " + id));
        itemRepository.delete(item);
        return ResponseEntity
                .ok()
                .header("Response Source", responseSource.toString())
                .build();
    }*/



}
