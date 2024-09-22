package com.bekrenovr.ecommerce.reviews.controller;

import com.bekrenovr.ecommerce.reviews.dto.ReviewRequest;
import com.bekrenovr.ecommerce.reviews.dto.ReviewResponse;
import com.bekrenovr.ecommerce.reviews.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<Page<ReviewResponse>> getReviewsForItem(
            @RequestParam("itemId") UUID itemId,
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "${custom.page.default-size}") int pageSize) {
        return ResponseEntity.ok(reviewService.getReviewsForItem(itemId, pageNumber, pageSize));
    }

    @PostMapping
    public ResponseEntity<Void> createReview(@RequestBody ReviewRequest request) {
        reviewService.createReview(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
