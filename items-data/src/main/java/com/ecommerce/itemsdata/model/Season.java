package com.ecommerce.itemsdata.model;

public enum Season {

    winter,
    spring,
    autumn,
    summer,
    multiseason;

    public static Season ofString(String s){
        return Season.valueOf(s.toLowerCase());
    }
}
