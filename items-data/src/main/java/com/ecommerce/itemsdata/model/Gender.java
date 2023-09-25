package com.ecommerce.itemsdata.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {

    MALE,
    FEMALE,
    UNISEX;

    public static Gender ofString(String s){
        return Gender.valueOf(s.toUpperCase());
    }
}
