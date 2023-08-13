package com.ecommerce.itemsdata.model;

public enum ColorEnum {

    black,
    white,
    red,
    yellow,
    green,
    blue,
    violet,
    grey,
    multi;
    public static ColorEnum ofString(String s){
        return ColorEnum.valueOf(s.toLowerCase());
    }
}
