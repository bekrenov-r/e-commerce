package com.bekrenovr.ecommerce.catalog.item.filters;

public enum Gender {

    MEN,
    WOMEN;

    public static Gender ofString(String s){
        return Gender.valueOf(s.toUpperCase());
    }
}
