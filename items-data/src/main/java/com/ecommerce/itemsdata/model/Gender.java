package com.ecommerce.itemsdata.model;

public enum Gender {

    MEN,
    WOMEN;

    public static Gender ofString(String s){
        return Gender.valueOf(s.toUpperCase());
    }
}
