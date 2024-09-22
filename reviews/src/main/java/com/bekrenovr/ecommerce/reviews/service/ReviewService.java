package com.bekrenovr.ecommerce.reviews.service;

import com.bekrenovr.ecommerce.common.util.PageUtil;
import com.bekrenovr.ecommerce.reviews.dto.ReviewMapper;
import com.bekrenovr.ecommerce.reviews.dto.ReviewRequest;
import com.bekrenovr.ecommerce.reviews.dto.ReviewResponse;
import com.bekrenovr.ecommerce.reviews.model.Review;
import com.bekrenovr.ecommerce.reviews.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public Page<ReviewResponse> getReviewsForItem(UUID itemId, int pageNumber, int pageSize) {
        List<ReviewResponse> reviews = reviewRepository.findAllByItemId(itemId)
                .stream()
                .map(reviewMapper::documentToResponse)
                .toList();
        return PageUtil.paginateList(reviews, pageNumber, pageSize);
    }

    public void createReview(ReviewRequest request) {
        Review review = reviewMapper.requestToDocument(request);
        review.setCustomerEmail("jane.doe@gmail.com");
        review.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(review);
    }
}
