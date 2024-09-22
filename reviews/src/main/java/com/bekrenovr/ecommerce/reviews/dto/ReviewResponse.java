package com.bekrenovr.ecommerce.reviews.dto;

import java.time.LocalDateTime;

public record ReviewResponse(
        String id,
        Object customer,
        int rating,
        String title,
        String content,
        LocalDateTime createdAt,
        int likes,
        int dislikes
) { }
