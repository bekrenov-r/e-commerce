package com.bekrenovr.ecommerce.orders.order.itementry;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import com.bekrenovr.ecommerce.orders.order.dto.CatalogItem;
import com.bekrenovr.ecommerce.orders.order.dto.UniqueItemResponse;

import static com.bekrenovr.ecommerce.orders.exception.OrdersApplicationExceptionReason.QUANTITY_IS_UNAVAILABLE;
import static com.bekrenovr.ecommerce.orders.exception.OrdersApplicationExceptionReason.SIZE_IS_UNAVAILABLE;

public class ItemEntryValidator {
    public static void validateEntryAgainstCatalogItem(ItemEntryRequest itemEntry, CatalogItem item) {
        int requestedQuantity = itemEntry.quantity();
        String requestedSize = itemEntry.size();
        UniqueItemResponse uniqueItemBySize = item.uniqueItems().stream()
                .filter(uniqueItem -> uniqueItem.size().equals(requestedSize))
                .findFirst()
                .orElseThrow(() -> new EcommerceApplicationException(SIZE_IS_UNAVAILABLE, requestedSize, item.id()));
        if(uniqueItemBySize.quantity() < requestedQuantity)
            throw new EcommerceApplicationException(QUANTITY_IS_UNAVAILABLE, item.id(), requestedSize);
    }
}
