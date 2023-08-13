package com.ecommerce.itemsdata.model;

public enum Material {

    denim,
    leather,
    wool,
    cotton,
    artificial_leather,
    synthetics;

    public static Material ofString(String s){
        return Material.valueOf(s.toLowerCase());
    }

}
