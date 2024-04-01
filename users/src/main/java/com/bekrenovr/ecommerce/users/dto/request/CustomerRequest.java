package com.bekrenovr.ecommerce.users.dto.request;

public record CustomerRequest(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        AddressRequest addressRequest
) { }
