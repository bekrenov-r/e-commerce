package com.ecommerce.itemsdata.service;

import com.ecommerce.itemsdata.dto.response.ItemResponse;
import com.ecommerce.itemsdata.dto.mapper.ItemToDtoMapper;
import com.ecommerce.itemsdata.model.*;
import com.ecommerce.itemsdata.repository.CategoryRepository;
import com.ecommerce.itemsdata.repository.ItemDetailsRepository;
import com.ecommerce.itemsdata.repository.ItemRepository;
import com.ecommerce.itemsdata.service.filter.ItemFilteringProcessor;
import com.ecommerce.itemsdata.service.sort.ItemSortComparators;
import com.ecommerce.itemsdata.service.sort.SortOption;
import com.ecommerce.itemsdata.util.dev.ItemGenerator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ItemDetailsRepository itemDetailsRepository;
    private final ItemGenerator itemGenerator;
    private final ItemToDtoMapper itemToDtoMapper;
    private final ItemFilteringProcessor itemFilteringProcessor;


    public List<ItemResponse> getAllItemsByGenderAndCategoryFilteredAndSorted(
            Gender gender, Long categoryId, SortOption sort, String priceRange, String sizes, String colors, String brands, Season season, String materials, Double rating
    ) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("No category found for id: " + categoryId));

        List<Item> items = itemFilteringProcessor
                .forItems(itemRepository.findAllByGenderAndCategory(gender, category))
                .withArgs(priceRange, sizes, colors, brands, season, materials, rating);

        items = items.stream()
                .sorted(sort.getComparator())
                .collect(Collectors.toList());

        return items.stream()
                .map(itemToDtoMapper::itemToResponse)
                .toList();
    }

    public Item getItemById(Long id) {
        return itemRepository.findById(id).orElse(null);
    }

    public ResponseEntity<Void> createItem(Item item) {
        item.setId(null);
        Item savedItem = itemRepository.save(item);
        ItemResponse itemDTO = itemToDtoMapper.itemToResponse(savedItem);

        /*// todo: get creatingEmployeeId from security context
        ItemDetails itemDetails = new ItemDetails(0, 0, LocalDateTime.now(), 0L);
        itemDetails.setItem(savedItem);
        itemDetailsRepository.save(itemDetails);*/

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedItem.getId())
                .toUri();
        return ResponseEntity.created(location).body(null);
    }

    // method for dev purpose
    public void createSampleItems(int quantity) {
        for(int i = 0; i < quantity; i++){
            Item item = itemGenerator.generateItemWithDetails();
            createItem(item);
        }
    }
}
