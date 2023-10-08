package com.ecommerce.itemsdata.model;

public enum AgeGroup {

    CHILDREN,
    ADULTS;

    public static AgeGroup ofString(String s){
        return AgeGroup.valueOf(s.toUpperCase());
    }

}
