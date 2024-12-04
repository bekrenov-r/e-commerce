package com.bekrenovr.ecommerce.reviews.review;

import com.bekrenovr.ecommerce.reviews.review.dto.ReviewRequest;
import com.bekrenovr.ecommerce.reviews.review.dto.ReviewResponse;
import com.bekrenovr.ecommerce.reviews.util.dev.ReviewResponsePageModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Tag(name = "ReviewController")
public class ReviewController {
    private final ReviewService reviewService;

    @Operation(summary = "Get all reviews for item")
    @ApiResponse(responseCode = "201", description = "Page with reviews",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ReviewResponsePageModel.class)))
    @ApiResponse(responseCode = "400", description = "Invalid parameter supplied",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @GetMapping
    public ResponseEntity<Page<ReviewResponse>> getAllForItem(
            @RequestParam("itemId") UUID itemId,
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "${custom.page.default-size}") int pageSize) {
        return ResponseEntity.ok(reviewService.getAllForItem(itemId, pageNumber, pageSize));
    }

    @Operation(summary = "Create review")
    @ApiResponse(responseCode = "201", description = "Review created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request body supplied / Cannot create review",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Customer already has review for this item",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Item does not exist",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid ReviewRequest request) {
        reviewService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Update review")
    @ApiResponse(responseCode = "200", description = "Review updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request body supplied",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Review not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "403", description = "Authenticated customer is not review owner",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> update(@PathVariable String id,
                                                 @RequestBody @Valid ReviewRequest request) {
        return ResponseEntity.ok(reviewService.update(id, request));
    }

    @Operation(summary = "Delete review")
    @ApiResponse(responseCode = "200", description = "Review deleted successfully")
    @ApiResponse(responseCode = "404", description = "Review not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "403", description = "Authenticated customer is not review owner",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ProblemDetail.class)))
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        reviewService.delete(id);
    }
}
