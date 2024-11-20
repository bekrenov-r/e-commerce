package com.bekrenovr.ecommerce.reviews.kafka;

import java.util.UUID;

public record RatingEvent(UUID itemId, double averageRating) { }
