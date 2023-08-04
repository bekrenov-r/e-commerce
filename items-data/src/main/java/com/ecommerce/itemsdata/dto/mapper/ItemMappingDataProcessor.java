package com.ecommerce.itemsdata.dto.mapper;

import com.ecommerce.itemsdata.model.Color;
import com.ecommerce.itemsdata.model.ColorEnum;
import com.ecommerce.itemsdata.model.Item;
import com.ecommerce.itemsdata.model.Size;
import com.ecommerce.itemsdata.proxy.CustomerServiceProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemMappingDataProcessor {

    private final CustomerServiceProxy customerServiceProxy;

    public List<ColorEnum> allColorsForItem(Item item){
        // todo: to be implemented
        return item.getColors().stream()
                .map(Color::getValue)
                .toList();
    }

    public List<String> allSizesForItem(Item item){
        // todo: to be implemented
        return item.getSizes().stream()
                .map(Size::getValue)
                .toList();
    }

    public boolean isItemOnWishList(Long itemId){
        // todo: get customerId from security
        Long customerId = null;
        return customerServiceProxy.isItemOnWishList(itemId, customerId);
    }

}
