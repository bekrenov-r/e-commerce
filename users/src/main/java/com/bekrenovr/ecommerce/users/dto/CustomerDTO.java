package com.bekrenovr.ecommerce.users.dto;

import java.util.UUID;

public record CustomerDTO(UUID id, String firstName, String lastName) {
}
