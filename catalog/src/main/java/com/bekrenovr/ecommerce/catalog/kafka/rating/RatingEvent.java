package com.bekrenovr.ecommerce.catalog.kafka.rating;

import java.util.UUID;

public record RatingEvent(UUID itemId, double averageRating) { }
