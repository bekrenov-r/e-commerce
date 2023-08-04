package com.ecommerce.itemsdata.proxy;

import org.springframework.stereotype.Component;

@Component
public class CustomerServiceProxy {

    public boolean isItemOnWishList(Long itemId, Long customerId){
        // todo: to be implemented as feign proxy
        return false;
    }

}
