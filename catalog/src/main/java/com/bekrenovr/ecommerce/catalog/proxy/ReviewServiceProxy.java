package com.bekrenovr.ecommerce.catalog.proxy;

import com.bekrenovr.ecommerce.catalog.dto.response.ReviewResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ReviewServiceProxy {
    public List<ReviewResponse> getReviewsForItem(UUID itemId) {
        return List.of();
    };
}
