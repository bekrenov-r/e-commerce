package com.bekrenovr.ecommerce.reviews.dto;

import com.bekrenovr.ecommerce.reviews.model.Review;
import com.bekrenovr.ecommerce.reviews.proxy.CustomersServiceProxy;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Mapper(componentModel = "spring")
public abstract class ReviewMapper {

    @Autowired
    protected CustomersServiceProxy customersProxy;

    public abstract ReviewResponse documentToResponse(Review review);

    public abstract Review requestToDocument(ReviewRequest request);

    @AfterMapping
    protected void setCustomer(Review review, @MappingTarget ReviewResponse response) {
        Map<String, String> customer = customersProxy.getCustomerByEmail(review.getCustomerEmail()).getBody();
        response.setCustomer(customer);
    }
}
