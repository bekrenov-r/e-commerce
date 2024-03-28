package com.bekrenovr.ecommerce.catalog.service;

import com.bekrenovr.ecommerce.catalog.dto.mapper.ItemToDtoMapper;
import com.bekrenovr.ecommerce.catalog.dto.request.FilterOptions;
import com.bekrenovr.ecommerce.catalog.dto.response.ItemResponse;
import com.bekrenovr.ecommerce.catalog.exception.ItemApplicationException;
import com.bekrenovr.ecommerce.catalog.model.entity.Category;
import com.bekrenovr.ecommerce.catalog.model.entity.Item;
import com.bekrenovr.ecommerce.catalog.model.entity.Subcategory;
import com.bekrenovr.ecommerce.catalog.model.enums.Gender;
import com.bekrenovr.ecommerce.catalog.repository.CategoryRepository;
import com.bekrenovr.ecommerce.catalog.repository.ItemDetailsRepository;
import com.bekrenovr.ecommerce.catalog.repository.ItemRepository;
import com.bekrenovr.ecommerce.catalog.service.sort.ItemSortComparators;
import com.bekrenovr.ecommerce.catalog.service.sort.SortOption;
import com.bekrenovr.ecommerce.catalog.util.PageUtil;
import com.bekrenovr.ecommerce.catalog.util.dev.ItemGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static com.bekrenovr.ecommerce.catalog.exception.ItemApplicationExceptionReason.CATEGORY_NOT_FOUND;
import static com.bekrenovr.ecommerce.catalog.exception.ItemApplicationExceptionReason.SUBCATEGORY_NOT_FOUND;
import static com.bekrenovr.ecommerce.catalog.repository.ItemSpecification.*;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ItemDetailsRepository itemDetailsRepository;
    private final ItemGenerator itemGenerator;
    private final ItemToDtoMapper itemToDtoMapper;

    @Value("${custom.page.size.main}")
    private Integer pageSizeMain;


    public Page<ItemResponse> getAllItemsByGenderAndCategory(
            Gender gender,
            UUID categoryId,
            SortOption sort,
            Integer page,
            FilterOptions filters
    ) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ItemApplicationException(CATEGORY_NOT_FOUND, categoryId));
        Specification<Item> specification = Specification.allOf(
                hasGender(gender), hasCategory(category), fromFilterOptions(filters)
        );
        List<Item> items = itemRepository.findAll(specification)
                .stream()
                .sorted(ItemSortComparators.forOption(sort))
                .toList();
        return PageUtil.paginateList(items, page, pageSizeMain)
                .map(itemToDtoMapper::itemToResponse);
    }

    public Page<ItemResponse> getAllItemsByGenderCategoryAndSubcategory(
            Gender gender,
            UUID categoryId,
            UUID subcategoryId,
            SortOption sort,
            Integer page,
            FilterOptions filters
    ) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ItemApplicationException(CATEGORY_NOT_FOUND, categoryId));
        Subcategory subcategory = category.getSubcategories().stream()
                .filter(sub -> sub.getId().equals(subcategoryId))
                .findFirst()
                .orElseThrow(() -> new ItemApplicationException(SUBCATEGORY_NOT_FOUND, subcategoryId, categoryId));
        Specification<Item> specification = Specification.allOf(
                hasGender(gender), hasCategoryAndSubcategory(category, subcategory), fromFilterOptions(filters)
        );
        List<Item> items = itemRepository.findAll(specification)
                .stream()
                .sorted(ItemSortComparators.forOption(sort))
                .toList();
        return PageUtil.paginateList(items, page, pageSizeMain)
                .map(itemToDtoMapper::itemToResponse);
    }

    public Item getItemById(UUID id) {
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

    // method for dev purpose
    public void createSampleItems(int quantity) {
        for(int i = 0; i < quantity; i++){
            Item item = itemGenerator.generateItemWithDetails();
            createItem(item);
        }
    }
}
