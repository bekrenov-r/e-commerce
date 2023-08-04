package com.ecommerce.itemsdata.model;

public enum Gender {

    male("M"),
    female("F");

    private final String gender;

    Gender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return gender;
    }
}
