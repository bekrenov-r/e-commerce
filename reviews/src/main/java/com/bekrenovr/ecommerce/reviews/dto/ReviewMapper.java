package com.bekrenovr.ecommerce.reviews.dto;

import com.bekrenovr.ecommerce.reviews.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "rating", source = "rating")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "likes", source = "likes")
    @Mapping(target = "dislikes", source = "dislikes")
    ReviewResponse documentToResponse(Review review);

    Review requestToDocument(ReviewRequest request);
}
