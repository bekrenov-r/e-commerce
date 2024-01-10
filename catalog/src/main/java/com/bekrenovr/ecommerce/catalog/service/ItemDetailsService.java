package com.bekrenovr.ecommerce.catalog.service;

import com.bekrenovr.ecommerce.catalog.repository.ItemDetailsRepository;
import org.springframework.stereotype.Service;

@Service
public class ItemDetailsService {

    private final ItemDetailsRepository itemDetailsRepository;

    public ItemDetailsService(ItemDetailsRepository itemDetailsRepository) {
        this.itemDetailsRepository = itemDetailsRepository;
    }
}
