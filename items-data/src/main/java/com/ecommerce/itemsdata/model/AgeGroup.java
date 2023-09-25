package com.ecommerce.itemsdata.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AgeGroup {

    CHILDREN,
    ADULTS;

    public static AgeGroup ofString(String s){
        return AgeGroup.valueOf(s.toUpperCase());
    }

}
