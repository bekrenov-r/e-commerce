package com.bekrenovr.ecommerce.reviews.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
public final class ReviewResponse {
    private String id;
    private Map<String, String> customer;
    private int rating;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private int likes;
    private int dislikes;
}
