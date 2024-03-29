package com.bekrenovr.ecommerce.catalog.model;

public record ShoesSize(int value) implements Size {
    @Override
    public String getSizeValue() {
        return String.valueOf(value);
    }
}
