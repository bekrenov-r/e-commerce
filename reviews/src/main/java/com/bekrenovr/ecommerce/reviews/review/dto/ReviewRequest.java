package com.bekrenovr.ecommerce.reviews.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ReviewRequest(
      @NotNull UUID itemId,
      @Min(1) @Max(5) int rating,
      @NotBlank String title,
      String content
) { }
