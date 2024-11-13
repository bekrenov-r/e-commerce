package com.bekrenovr.ecommerce.catalog.item;

import com.bekrenovr.ecommerce.catalog.item.details.ItemDetails;
import com.bekrenovr.ecommerce.catalog.item.details.ItemDetailsRepository;
import com.bekrenovr.ecommerce.catalog.item.filters.FilterOptions;
import com.bekrenovr.ecommerce.catalog.item.metadata.ItemMetadata;
import com.bekrenovr.ecommerce.catalog.item.metadata.ItemMetadataService;
import com.bekrenovr.ecommerce.catalog.item.sorting.ItemSortComparators;
import com.bekrenovr.ecommerce.catalog.item.sorting.SortOption;
import com.bekrenovr.ecommerce.common.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemSpecificationBuilder itemSpecificationBuilder;
    private final ItemMapper itemMapper;
    private final ItemMetadataService metadataService;
    private final ItemDetailsRepository itemDetailsRepository;

    public Page<ItemResponse> getItemsByCriteria(
            SortOption sort, Integer pageNumber, Integer pageSize, FilterOptions filters
    ) {
        Specification<Item> specification = itemSpecificationBuilder.buildFromFilterOptions(filters);
        List<Item> items = itemRepository.findAll(specification)
                .stream()
                .sorted(ItemSortComparators.forOption(sort))
                .toList();
        Page<Item> paginatedItems = PageUtil.paginateList(items, pageNumber, pageSize);
        Map<Item, ItemMetadata> metadataMap = metadataService.generateMetadata(paginatedItems);
        return paginatedItems.map(item -> itemMapper.itemToResponse(item, metadataMap.get(item)));
    }

    public ItemDetailedResponse getById(UUID id) {
        Item item = itemRepository.findByIdOrThrowDefault(id);
        ItemMetadata metadata = metadataService.generateMetadata(item);
        return itemMapper.itemToDetailedResponse(item, metadata);
    }

    public List<ItemResponse> getByIds(List<UUID> ids) {
        List<Item> items = itemRepository.findAllById(ids);
        Map<Item, ItemMetadata> metadataMap = metadataService.generateMetadata(items);
        return items.stream()
                .map(item -> itemMapper.itemToResponse(item, metadataMap.get(item)))
                .toList();
    }

    public void create(Item item) {
        Item savedItem = itemRepository.save(item);
        this.createItemDetails(savedItem);
    }

    private void createItemDetails(Item item) {
        ItemDetails itemDetails = new ItemDetails(0, 0, LocalDateTime.now(), "john.doe@example.com");
        itemDetails.setItem(item);
        itemDetailsRepository.save(itemDetails);
    }
}
