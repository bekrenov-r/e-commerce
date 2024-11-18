package com.bekrenovr.ecommerce.catalog.item.details;

import com.bekrenovr.ecommerce.catalog.item.Item;
import com.bekrenovr.ecommerce.common.security.AuthenticationUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ItemDetailsService {

    private final ItemDetailsRepository itemDetailsRepository;

    public ItemDetailsService(ItemDetailsRepository itemDetailsRepository) {
        this.itemDetailsRepository = itemDetailsRepository;
    }

    public Item addItemDetails(Item item) {
        String currentEmployeeUsername = AuthenticationUtil.getAuthenticatedUser().getUsername();
        ItemDetails itemDetails = new ItemDetails(0, 0, LocalDateTime.now(), currentEmployeeUsername);
        itemDetails.setItem(item);
        item.setItemDetails(itemDetailsRepository.save(itemDetails));
        return item;
    }
}
