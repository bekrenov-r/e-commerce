package com.ecommerce.itemsdata.service;

import com.ecommerce.itemsdata.repository.ItemDetailsRepository;
import org.springframework.stereotype.Service;

@Service
public class ItemDetailsService {

    private final ItemDetailsRepository itemDetailsRepository;

    public ItemDetailsService(ItemDetailsRepository itemDetailsRepository) {
        this.itemDetailsRepository = itemDetailsRepository;
    }
}
