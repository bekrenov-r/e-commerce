package com.ecommerce.userdata.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "address")
@NoArgsConstructor
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "country")
    @NotNull(message = "Country can't be null")
    private String country;

    @Column(name = "city")
    @NotNull(message = "City can't be null")
    private String city;

    @Column(name = "street")
    @NotNull(message = "Street can't be null")
    private String street;

    @Column(name = "building_number")
    @NotNull(message = "Building number can't be null")
    private String buildingNumber;

    @Column(name = "flat_number")
    private String flatNumber;

    @Column(name = "postal_code")
    @NotNull(message = "Postal code can't be null")
    @Pattern(regexp = "^[0-9]{2}-[0-9]{3}$", message = "Invalid postal code format for address")
    private String postalCode;

    public Address(String country, String city, String street, String buildingNumber, String flatNumber, String postalCode) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.buildingNumber = buildingNumber;
        this.flatNumber = flatNumber;
        this.postalCode = postalCode;
    }
}
