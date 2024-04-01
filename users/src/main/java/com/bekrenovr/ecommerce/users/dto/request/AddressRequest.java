package com.bekrenovr.ecommerce.users.dto.request;

public record AddressRequest(
        String countryCode,
        String city,
        String street,
        String buildingNumber,
        String flatNumber,
        String zipCode
) { }
