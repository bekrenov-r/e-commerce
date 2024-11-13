package com.bekrenovr.ecommerce.catalog.item.size;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ClothesSize implements Size {
    XS(1),
    S(2),
    M(3),
    L(4),
    XL(5),
    XXL(6),
    XXXL(7),
    XXXXL(8);

    private final int numericValue;

    @Override
    public String getStringValue() {
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
