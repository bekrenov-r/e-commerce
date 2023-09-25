package com.ecommerce.itemsdata.model;

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
