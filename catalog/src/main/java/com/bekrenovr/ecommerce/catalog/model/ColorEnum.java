package com.bekrenovr.ecommerce.catalog.model;

public enum ColorEnum {

    BLACK,
    WHITE,
    RED,
    YELLOW,
    GREEN,
    BLUE,
    VIOLET,
    GREY,
    MULTI;
    public static ColorEnum ofString(String s){
        return ColorEnum.valueOf(s.toUpperCase());
    }
}
