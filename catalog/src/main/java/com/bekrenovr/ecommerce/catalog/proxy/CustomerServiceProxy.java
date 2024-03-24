package com.bekrenovr.ecommerce.catalog.proxy;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CustomerServiceProxy {

    public boolean isItemOnWishList(UUID itemId, UUID customerId){
        // todo: to be implemented as feign proxy
        return false;
    }

}
