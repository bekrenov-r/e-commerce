package com.bekrenovr.ecommerce.customers.dto.response;

import java.util.UUID;

public record CustomerResponse(
        UUID id,
        String firstName,
        String lastName,
        String email
) { }
