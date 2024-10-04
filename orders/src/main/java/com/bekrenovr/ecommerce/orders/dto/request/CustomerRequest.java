package com.bekrenovr.ecommerce.orders.dto.request;

import lombok.Data;

@Data
public final class CustomerRequest {
    private String firstName;
    private String lastName;
    private String email;
    private boolean isRegistered;
}