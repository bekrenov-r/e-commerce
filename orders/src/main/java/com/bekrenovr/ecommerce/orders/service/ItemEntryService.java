package com.bekrenovr.ecommerce.orders.service;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import com.bekrenovr.ecommerce.orders.dto.mapper.ItemEntryMapper;
import com.bekrenovr.ecommerce.orders.dto.request.ItemEntryRequest;
import com.bekrenovr.ecommerce.orders.dto.response.ItemResponse;
import com.bekrenovr.ecommerce.orders.model.entity.ItemEntry;
import com.bekrenovr.ecommerce.orders.proxy.CatalogProxy;
import com.bekrenovr.ecommerce.orders.validation.ItemEntryValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.bekrenovr.ecommerce.orders.exception.OrdersApplicationExceptionReason.NON_EXISTENT_ITEMS_IN_ORDER;

@Service
@RequiredArgsConstructor
public class ItemEntryService {
    private final CatalogProxy catalogProxy;
    private final ItemEntryMapper itemEntryMapper;

    public List<ItemEntry> createItemEntries(List<ItemEntryRequest> itemEntryRequests) {
        List<UUID> itemsIds = itemEntryRequests.stream()
                .map(ItemEntryRequest::itemId)
                .toList();
        List<ItemResponse> items = catalogProxy.getItemsByIds(itemsIds).getBody();
        if(items.size() < itemEntryRequests.size())
            throw new EcommerceApplicationException(NON_EXISTENT_ITEMS_IN_ORDER);
        List<ItemEntry> itemEntries = new ArrayList<>();
        for(int i = 0; i < items.size(); i++) {
            ItemEntryValidator.validateEntryAgainstItem(itemEntryRequests.get(i), items.get(i));
            int quantity = itemEntryRequests.get(i).quantity();
            String size = itemEntryRequests.get(i).size();
            ItemEntry itemEntry = itemEntryMapper.itemResponseToEntity(items.get(i), quantity, size);
            itemEntries.add(itemEntry);
        }
        return itemEntries;
    }
}
