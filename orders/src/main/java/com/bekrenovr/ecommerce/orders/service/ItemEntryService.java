package com.bekrenovr.ecommerce.orders.service;

import com.bekrenovr.ecommerce.catalog.dto.response.ItemResponse;
import com.bekrenovr.ecommerce.catalog.dto.response.UniqueItemShortResponse;
import com.bekrenovr.ecommerce.catalog.model.Size;
import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import com.bekrenovr.ecommerce.orders.dto.mapper.ItemEntryMapper;
import com.bekrenovr.ecommerce.orders.dto.request.ItemEntryRequest;
import com.bekrenovr.ecommerce.orders.model.entity.ItemEntry;
import com.bekrenovr.ecommerce.orders.proxy.ItemProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.bekrenovr.ecommerce.orders.exception.OrdersApplicationExceptionReason.*;

@Service
@RequiredArgsConstructor
public class ItemEntryService {
    private final ItemProxy itemProxy;
    private final ItemEntryMapper itemEntryMapper;

    public List<ItemEntry> createItemEntries(List<ItemEntryRequest> itemEntryRequests) {
        List<UUID> itemsIds = itemEntryRequests.stream()
                .map(ItemEntryRequest::itemId)
                .toList();
        List<ItemResponse> items = itemProxy.getItemsByIds(itemsIds).getBody();
        if(items.size() < itemEntryRequests.size())
            throw new EcommerceApplicationException(NON_EXISTENT_ITEMS_IN_ORDER);
        List<ItemEntry> itemEntries = new ArrayList<>();
        for(int i = 0; i < items.size(); i++) {
            validateEntryAgainstItem(itemEntryRequests.get(i), items.get(i));
            int quantity = itemEntryRequests.get(i).quantity();
            Size size = itemEntryRequests.get(i).size();
            ItemEntry itemEntry = itemEntryMapper.itemResponseToEntity(items.get(i), quantity, size);
            itemEntries.add(itemEntry);
        }
        return itemEntries;
    }

    private void validateEntryAgainstItem(ItemEntryRequest itemEntry, ItemResponse item) {
        Size requestedSize = itemEntry.size();
        int requestedQuantity = itemEntry.quantity();
        UniqueItemShortResponse uniqueItemBySize = item.uniqueItems().stream()
                .filter(uniqueItem -> uniqueItem.size().equals(requestedSize.getSizeValue()))
                .findFirst()
                .orElseThrow(() -> new EcommerceApplicationException(SIZE_IS_UNAVAILABLE, requestedSize.getSizeValue(), item.id()));
        if(uniqueItemBySize.quantity() < requestedQuantity)
            throw new EcommerceApplicationException(QUANTITY_IS_UNAVAILABLE, item.id(), requestedSize.getSizeValue());
    }
}
