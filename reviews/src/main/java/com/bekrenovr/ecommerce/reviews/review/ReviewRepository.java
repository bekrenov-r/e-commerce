package com.bekrenovr.ecommerce.reviews.review;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findAllByItemId(UUID itemId);
    List<Review> findAllByCustomerEmail(String customerEmail);
    boolean existsByItemIdAndCustomerEmail(UUID itemId, String customerEmail);
}
