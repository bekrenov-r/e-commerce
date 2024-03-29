package com.bekrenovr.ecommerce.catalog.model.enums;

import com.bekrenovr.ecommerce.catalog.model.Size;

public enum ClothesSize implements Size {
    XS, S, M, L, XL, XXL, XXXL, XXXXL;

    @Override
    public String getSizeValue() {
        return name();
    }

    public static ClothesSize ofString(String s) {
        return switch(s){
            case "4XL" -> XXXXL;
            case "3XL" -> XXXL;
            case "2XL" -> XXL;
            default -> ClothesSize.valueOf(s);
        };
    }
}
