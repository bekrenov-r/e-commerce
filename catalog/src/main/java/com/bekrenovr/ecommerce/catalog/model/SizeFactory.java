package com.bekrenovr.ecommerce.catalog.model;

import com.bekrenovr.ecommerce.catalog.model.enums.ClothesSize;
import com.bekrenovr.ecommerce.catalog.model.enums.SizeType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.stream.Stream;

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

    public List<Size> getAllShoesSizes(){
        return Stream
                .iterate(
                        shoesSizeMin,
                        i -> i <= shoesSizeMax,
                        i -> i + 1
                ).map(this::getShoesSize)
                .toList();
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
