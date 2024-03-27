package com.bekrenovr.ecommerce.catalog.model.enums;

public enum Season {

    WINTER,
    SPRING,
    AUTUMN,
    SUMMER,
    MULTISEASON;

    public static Season ofString(String s){
        return Season.valueOf(s.toUpperCase());
    }
}
