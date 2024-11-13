package com.bekrenovr.ecommerce.catalog.item.metadata;

import com.bekrenovr.ecommerce.catalog.feign.CustomersProxy;
import com.bekrenovr.ecommerce.catalog.item.Item;
import com.bekrenovr.ecommerce.catalog.item.ItemResponse;
import com.bekrenovr.ecommerce.catalog.item.uniqueitem.UniqueItem;
import com.bekrenovr.ecommerce.common.security.AuthenticationUtil;
import com.bekrenovr.ecommerce.common.security.Role;
import com.bekrenovr.ecommerce.common.security.SecurityConstants;
import com.bekrenovr.ecommerce.common.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ItemMetadataService {
    private final CustomersProxy customersProxy;

    @Value("${custom.strategy.popularity.item-is-considered-popular-at-orders-count}")
    private int popularItemOrdersCount;

    public Map<Item, ItemMetadata> generateMetadata(Iterable<Item> items) {
        Map<Item, ItemMetadata> map = StreamSupport.stream(items.spliterator(), false)
                .collect(Collectors.toMap(item -> item, item -> new ItemMetadata()));
        if(shouldIncludeWishListData()){
            Set<UUID> wishListItemsIds = getWishListItems().stream()
                    .map(ItemResponse::getId)
                    .collect(Collectors.toSet());
            map.forEach((item, metadata) -> {
                boolean isOnWishList = wishListItemsIds.contains(item.getId());
                metadata.setOnWishList(isOnWishList);
            });
        }
        map.forEach((item, metadata) -> {
            metadata.setNew(isNew(item));
            metadata.setAvailable(isAvailable(item));
            metadata.setPopular(isPopular(item));
        });
        return map;
    }

    public ItemMetadata generateMetadata(Item item) {
        ItemMetadata metadata = new ItemMetadata();
        if(shouldIncludeWishListData()) {
            Set<UUID> wishListItemsIds = getWishListItems().stream()
                    .map(ItemResponse::getId)
                    .collect(Collectors.toSet());
            boolean isOnWishList = wishListItemsIds.contains(item.getId());
            metadata.setOnWishList(isOnWishList);
        }
        metadata.setNew(isNew(item));
        metadata.setAvailable(isAvailable(item));
        metadata.setPopular(isPopular(item));
        return metadata;
    }

    private List<ItemResponse> getWishListItems(){
        String authenticatedUserHeader =
                RequestUtil.getCurrentRequest().getHeader(SecurityConstants.AUTHENTICATED_USER_HEADER);
        List<ItemResponse> items = customersProxy.getWishListItems(authenticatedUserHeader).getBody();
        return items != null ? items : List.of();
    }

    private boolean isNew(Item item){
        LocalDate createdDate = item.getItemDetails().getCreatedAt().toLocalDate();
        return LocalDate.now().toEpochDay() - createdDate.toEpochDay() < 30;
    }

    private boolean isAvailable(Item item){
        int itemQuantity = item.getUniqueItems().stream()
                .mapToInt(UniqueItem::getQuantity)
                .sum();
        return itemQuantity > 0;
    }

    private boolean isPopular(Item item){
        return item.getItemDetails().getOrdersCountLastMonth() >= popularItemOrdersCount;
    }

    private boolean shouldIncludeWishListData() {
        return AuthenticationUtil.requestHasAuthentication()
                && AuthenticationUtil.getAuthenticatedUser().hasRole(Role.CUSTOMER);
    }
}
