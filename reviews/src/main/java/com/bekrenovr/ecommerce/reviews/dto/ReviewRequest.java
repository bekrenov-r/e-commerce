package com.bekrenovr.ecommerce.reviews.dto;

import java.util.UUID;

public record ReviewRequest(
      UUID itemId,
      int rating,
      String title,
      String content
) { }
