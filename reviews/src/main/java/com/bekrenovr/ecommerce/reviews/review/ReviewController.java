package com.bekrenovr.ecommerce.reviews.review;

import com.bekrenovr.ecommerce.reviews.review.dto.ReviewRequest;
import com.bekrenovr.ecommerce.reviews.review.dto.ReviewResponse;
import jakarta.validation.Valid;
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
    public ResponseEntity<Page<ReviewResponse>> getAllForItem(
            @RequestParam("itemId") UUID itemId,
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "${custom.page.default-size}") int pageSize) {
        return ResponseEntity.ok(reviewService.getAllForItem(itemId, pageNumber, pageSize));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid ReviewRequest request) {
        reviewService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> update(@PathVariable String id,
                                                 @RequestBody @Valid ReviewRequest request) {
        return ResponseEntity.ok(reviewService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        reviewService.delete(id);
    }
}
