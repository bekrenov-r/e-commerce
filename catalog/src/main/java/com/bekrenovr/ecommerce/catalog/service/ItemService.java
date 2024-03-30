package com.bekrenovr.ecommerce.catalog.service;

import com.bekrenovr.ecommerce.catalog.dto.mapper.ItemMapper;
import com.bekrenovr.ecommerce.catalog.dto.request.FilterOptions;
import com.bekrenovr.ecommerce.catalog.dto.response.ItemResponse;
import com.bekrenovr.ecommerce.catalog.jpa.repository.ItemRepository;
import com.bekrenovr.ecommerce.catalog.jpa.specification.ItemSpecificationBuilder;
import com.bekrenovr.ecommerce.catalog.model.entity.Item;
import com.bekrenovr.ecommerce.catalog.service.sort.ItemSortComparators;
import com.bekrenovr.ecommerce.catalog.service.sort.SortOption;
import com.bekrenovr.ecommerce.catalog.util.PageUtil;
import com.bekrenovr.ecommerce.catalog.util.dev.ItemGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemSpecificationBuilder itemSpecificationBuilder;
    private final ItemGenerator itemGenerator;
    private final ItemMapper itemMapper;

    public Page<ItemResponse> getItemsByCriteria(
            SortOption sort, Integer pageNumber, Integer pageSize, FilterOptions filters
    ) {
        Specification<Item> specification = itemSpecificationBuilder.buildFromFilterOptions(filters);
        List<Item> items = itemRepository.findAll(specification)
                .stream()
                .sorted(ItemSortComparators.forOption(sort))
                .toList();
        return PageUtil.paginateList(items, pageNumber, pageSize)
                .map(itemMapper::itemToResponse);
    }

    public Item getItemById(UUID id) {
        return itemRepository.findById(id).orElse(null);
    }

    public ResponseEntity<Void> createItem(Item item) {
        item.setId(null);
        Item savedItem = itemRepository.save(item);
//        ItemResponse itemDTO = itemMapper.itemToResponse(savedItem);

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
