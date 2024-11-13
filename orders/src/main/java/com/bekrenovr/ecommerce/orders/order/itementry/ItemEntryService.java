package com.bekrenovr.ecommerce.orders.order.itementry;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import com.bekrenovr.ecommerce.orders.feign.CatalogProxy;
import com.bekrenovr.ecommerce.orders.order.dto.CatalogItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.bekrenovr.ecommerce.orders.exception.OrdersApplicationExceptionReason.NON_EXISTENT_ITEMS_IN_ORDER;

@Service
@RequiredArgsConstructor
public class ItemEntryService {
    private final CatalogProxy catalogProxy;

    public Map<ItemEntryRequest, CatalogItem> processRequests(List<ItemEntryRequest> requests) {
        Map<ItemEntryRequest, CatalogItem> map = this.mapItemEntryRequestsToCatalogItems(requests);
        map.forEach(ItemEntryValidator::validateEntryAgainstCatalogItem);
        return map;
    }

    private Map<ItemEntryRequest, CatalogItem> mapItemEntryRequestsToCatalogItems(List<ItemEntryRequest> itemEntryRequests) {
        List<UUID> itemsIds = itemEntryRequests.stream()
                .map(ItemEntryRequest::itemId)
                .toList();
        Map<UUID, CatalogItem> catalogItems = catalogProxy.getItemsByIds(itemsIds).getBody()
                .stream()
                .collect(Collectors.toMap(CatalogItem::id, ca -> ca));
        if(catalogItems.size() < itemEntryRequests.size()){
            throw new EcommerceApplicationException(NON_EXISTENT_ITEMS_IN_ORDER);
        }
        return itemEntryRequests.stream()
                .collect(Collectors.toMap(request -> request, request -> catalogItems.get(request.itemId())));
    }
}
