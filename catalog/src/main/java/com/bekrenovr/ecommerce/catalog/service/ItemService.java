package com.bekrenovr.ecommerce.catalog.service;

import com.bekrenovr.ecommerce.catalog.dto.mapper.ItemToDtoMapper;
import com.bekrenovr.ecommerce.catalog.dto.request.FilterOptionsModel;
import com.bekrenovr.ecommerce.catalog.dto.response.ItemResponse;
import com.bekrenovr.ecommerce.catalog.exception.ItemApplicationException;
import com.bekrenovr.ecommerce.catalog.exception.ItemApplicationExceptionReason;
import com.bekrenovr.ecommerce.catalog.model.Category;
import com.bekrenovr.ecommerce.catalog.model.Gender;
import com.bekrenovr.ecommerce.catalog.model.Item;
import com.bekrenovr.ecommerce.catalog.model.Subcategory;
import com.bekrenovr.ecommerce.catalog.repository.CategoryRepository;
import com.bekrenovr.ecommerce.catalog.repository.ItemDetailsRepository;
import com.bekrenovr.ecommerce.catalog.repository.ItemRepository;
import com.bekrenovr.ecommerce.catalog.service.filter.ItemFilter;
import com.bekrenovr.ecommerce.catalog.service.sort.ItemSortComparators;
import com.bekrenovr.ecommerce.catalog.service.sort.SortOption;
import com.bekrenovr.ecommerce.catalog.util.dev.ItemGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ItemDetailsRepository itemDetailsRepository;
    private final ItemGenerator itemGenerator;
    private final ItemToDtoMapper itemToDtoMapper;

    @Value("${custom.page.size}")
    private Integer pageSize;


    public Page<ItemResponse> getAllItemsByGenderAndCategory(
            Gender gender,
            String categoryId,
            SortOption sort,
            Integer page,
            FilterOptionsModel filters
    ) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ItemApplicationException(ItemApplicationExceptionReason.CATEGORY_NOT_FOUND, categoryId));
        var items = itemRepository.findAllByGenderAndCategory(gender, category);
        return this.processItems(items, filters, page, sort);
    }

    public Page<ItemResponse> getAllItemsByGenderCategoryAndSubcategory(
            Gender gender,
            String categoryId,
            Long subcategoryId,
            SortOption sort,
            Integer page,
            FilterOptionsModel filters
    ) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ItemApplicationException(ItemApplicationExceptionReason.CATEGORY_NOT_FOUND, categoryId));
        Subcategory subcategory = category.getSubcategories().stream()
                .filter(sub -> sub.getId().equals(subcategoryId))
                .findFirst()
                .orElseThrow(() -> new ItemApplicationException(ItemApplicationExceptionReason.SUBCATEGORY_NOT_FOUND, subcategoryId, categoryId));
        var items = itemRepository.findAllByGenderAndCategoryAndSubcategory(gender, category, subcategory);
        return this.processItems(items, filters, page, sort);
    }

    public Item getItemById(Long id) {
        return itemRepository.findById(id).orElse(null);
    }

    public ResponseEntity<Void> createItem(Item item) {
        item.setId(null);
        Item savedItem = itemRepository.save(item);
//        ItemResponse itemDTO = itemToDtoMapper.itemToResponse(savedItem);

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

    private Page<ItemResponse> processItems(List<Item> items, FilterOptionsModel filters, Integer page, SortOption sort){
        var filteredItems = this.filterItems(items, filters);
        var sortedItems = filteredItems.stream()
                .sorted(ItemSortComparators.forOption(sort))
                .toList();
        Pageable pageRequest = PageRequest.of(page, this.pageSize);
        List<Item> pageContent = this.getSublistForPageRequest(sortedItems, pageRequest);
        var itemResponses = pageContent.stream()
                .map(itemToDtoMapper::itemToResponse)
                .toList();
        return new PageImpl<>(itemResponses, pageRequest, items.size());
    }

    private List<Item> filterItems(List<Item> items, FilterOptionsModel filters) {
        ItemFilter processor = new ItemFilter(items);
        return processor.filter(filters);
    }

    private List<Item> getSublistForPageRequest(List<Item> items, Pageable pageRequest){
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), items.size());
        return items.subList(start, end);
    }

    // method for dev purpose
    public void createSampleItems(int quantity) {
        for(int i = 0; i < quantity; i++){
            Item item = itemGenerator.generateItemWithDetails();
            createItem(item);
        }
    }
}
