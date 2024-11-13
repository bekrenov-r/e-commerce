package com.bekrenovr.ecommerce.catalog.item.details;

import org.springframework.stereotype.Service;

@Service
public class ItemDetailsService {

    private final ItemDetailsRepository itemDetailsRepository;

    public ItemDetailsService(ItemDetailsRepository itemDetailsRepository) {
        this.itemDetailsRepository = itemDetailsRepository;
    }
}
