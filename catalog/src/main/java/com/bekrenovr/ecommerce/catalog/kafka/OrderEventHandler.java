package com.bekrenovr.ecommerce.catalog.kafka;

import com.bekrenovr.ecommerce.catalog.item.uniqueitem.UniqueItem;
import com.bekrenovr.ecommerce.catalog.item.uniqueitem.UniqueItemRepository;
import com.bekrenovr.ecommerce.catalog.kafka.model.OrderEvent;
import com.bekrenovr.ecommerce.catalog.kafka.model.OrderItemEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderEventHandler {
    private final UniqueItemRepository uniqueItemRepository;

    public void handleOrderCreation(OrderEvent orderEvent) {
        Map<OrderItemEntry, UniqueItem> map = orderEvent.getItemEntries()
                .stream()
                .collect(Collectors.toMap(itemEntry -> itemEntry, this::mapItemEntryToUniqueItem));
        map.forEach((itemEntry, uniqueItem) -> decreaseUniqueItemQuantity(uniqueItem, itemEntry.quantity()));
    }

    public void handleOrderCancellation(OrderEvent orderEvent) {
        Map<OrderItemEntry, UniqueItem> map = orderEvent.getItemEntries()
                .stream()
                .collect(Collectors.toMap(itemEntry -> itemEntry, this::mapItemEntryToUniqueItem));
        map.forEach((itemEntry, uniqueItem) -> increaseItemQuantity(uniqueItem, itemEntry.quantity()));
    }

    private void increaseItemQuantity(UniqueItem uniqueItem, int quantity) {
        uniqueItem.setQuantity(uniqueItem.getQuantity() + quantity);
        uniqueItemRepository.save(uniqueItem);
    }

    private void decreaseUniqueItemQuantity(UniqueItem uniqueItem, int quantity) {
        uniqueItem.setQuantity(uniqueItem.getQuantity() - quantity);
        uniqueItemRepository.save(uniqueItem);
    }

    private UniqueItem mapItemEntryToUniqueItem(OrderItemEntry itemEntry) {
        return uniqueItemRepository.findByItemIdAndSize(itemEntry.itemId(), itemEntry.size());
    }
}
