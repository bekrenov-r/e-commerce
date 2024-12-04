package com.bekrenovr.ecommerce.reviews.util.dev;

import com.bekrenovr.ecommerce.reviews.review.dto.ReviewResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public class ReviewResponsePageModel extends PageImpl<ReviewResponse> {
    @ArraySchema(schema = @Schema(implementation = ReviewResponse.class))
    private List<ReviewResponse> content;

    public ReviewResponsePageModel(List<ReviewResponse> content) {
        super(content);
    }
}
