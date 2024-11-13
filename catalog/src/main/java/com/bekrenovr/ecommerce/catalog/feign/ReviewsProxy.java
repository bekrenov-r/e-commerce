package com.bekrenovr.ecommerce.catalog.feign;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ReviewsProxy {
    public List<?> getReviewsForItem(UUID itemId) {
        return List.of();
    };
}
