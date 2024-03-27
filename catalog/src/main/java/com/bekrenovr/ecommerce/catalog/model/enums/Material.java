package com.bekrenovr.ecommerce.catalog.model.enums;

public enum Material {

    DENIM,
    LEATHER,
    WOOL,
    COTTON,
    ARTIFICIAL_LEATHER,
    SYNTHETICS;

    public static Material ofString(String s){
        return Material.valueOf(s.toUpperCase());
    }

}
