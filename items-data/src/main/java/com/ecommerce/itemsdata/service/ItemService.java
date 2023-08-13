package com.ecommerce.itemsdata.service;

import com.ecommerce.itemsdata.dto.request.FilterOptionsModel;
import com.ecommerce.itemsdata.dto.response.ItemResponse;
import com.ecommerce.itemsdata.dto.mapper.ItemToDtoMapper;
import com.ecommerce.itemsdata.exception.ItemApplicationException;
import com.ecommerce.itemsdata.model.*;
import com.ecommerce.itemsdata.repository.CategoryRepository;
import com.ecommerce.itemsdata.repository.ItemDetailsRepository;
import com.ecommerce.itemsdata.repository.ItemRepository;
import com.ecommerce.itemsdata.service.filter.ItemFilter;
import com.ecommerce.itemsdata.service.sort.ItemSortComparators;
import com.ecommerce.itemsdata.service.sort.SortOption;
import com.ecommerce.itemsdata.util.dev.ItemGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.ecommerce.itemsdata.exception.ItemApplicationExceptionReason.*;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ItemDetailsRepository itemDetailsRepository;
    private final ItemGenerator itemGenerator;
    private final ItemToDtoMapper itemToDtoMapper;


    public List<ItemResponse> getAllItemsByGenderAndCategory(
            Gender gender,
            Long categoryId,
            SortOption sort,
            Integer page,
            FilterOptionsModel filters
    ) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ItemApplicationException(CATEGORY_NOT_FOUND, categoryId));
        var items = itemRepository.findAllByGenderAndCategory(gender, category);
        var filteredItems = this.filterItems(items, filters);
        return filteredItems.stream()
                .sorted(ItemSortComparators.forOption(sort))
                .map(itemToDtoMapper::itemToResponse)
                .toList();
    }

    public List<ItemResponse> getAllItemsByGenderCategoryAndSubcategory(
            Gender gender,
            Long categoryId,
            Long subcategoryId,
            SortOption sort,
            Integer page,
            FilterOptionsModel filters
    ) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ItemApplicationException(CATEGORY_NOT_FOUND, categoryId));

        Subcategory subcategory = category.getSubcategories().stream()
                .filter(sub -> sub.getId().equals(subcategoryId))
                .findFirst()
                .orElseThrow(() -> new ItemApplicationException(SUBCATEGORY_NOT_FOUND, subcategoryId, categoryId));

        var items = itemRepository.findAllByGenderAndCategoryAndSubcategory(gender, category, subcategory);
        var filteredItems = this.filterItems(items, filters);
        return filteredItems.stream()
                .sorted(ItemSortComparators.forOption(sort))
                .map(itemToDtoMapper::itemToResponse)
                .toList();
    }

    public List<ItemResponse> getAllItemsByAgeGenderAndCategory(
            AgeGroup ageGroup,
            Gender gender,
            Long categoryId,
            SortOption sort,
            Integer page,
            FilterOptionsModel filters
    ) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ItemApplicationException(CATEGORY_NOT_FOUND, categoryId));
        var items = itemRepository.findAllByAgeGroupAndGenderAndCategory(ageGroup, gender, category);
        var filteredItems = filterItems(items, filters);
        return filteredItems.stream()
                .sorted(ItemSortComparators.forOption(sort))
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

    private List<Item> filterItems(List<Item> items, FilterOptionsModel filters) {
        ItemFilter processor = new ItemFilter(items);
        return processor.filter(filters);
    }

    // method for dev purpose
    public void createSampleItems(int quantity) {
        for(int i = 0; i < quantity; i++){
            Item item = itemGenerator.generateItemWithDetails();
            createItem(item);
        }
    }
}
