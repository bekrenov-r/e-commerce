package com.bekrenovr.ecommerce.catalog.item.size;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;

@RequiredArgsConstructor
public class SizeFactory {
    private final int shoesSizeMin;
    private final int shoesSizeMax;

    public Size getSize(SizeType type, String value){
        return type.equals(SizeType.CLOTHES) ? getClothesSize(value) : getShoesSize(value);
    }

    public Size getSize(String value){
        return StringUtils.isNumeric(value) ? getShoesSize(value) : getClothesSize(value);
    }

    private Size getClothesSize(String value){
        return ClothesSize.ofString(value);
    }

    private Size getShoesSize(String value){
        int numericValue = Integer.parseInt(value);
        return getShoesSize(numericValue);
    }

    private Size getShoesSize(int value){
        if(value < shoesSizeMin || value > shoesSizeMax)
            throw new IllegalArgumentException("This size is not supported");

        return new ShoesSize(value);
    }
}
