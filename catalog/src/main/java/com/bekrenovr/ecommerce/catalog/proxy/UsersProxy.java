package com.bekrenovr.ecommerce.catalog.proxy;

import com.bekrenovr.ecommerce.catalog.dto.response.ItemResponse;
import com.bekrenovr.ecommerce.common.security.SecurityConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient("users")
public interface UsersProxy {
    @GetMapping("/users/wishlist")
    ResponseEntity<List<ItemResponse>> getWishListItems(
            @RequestHeader(SecurityConstants.AUTHENTICATED_USER_HEADER)
            String authenticatedUserHeader
    );

}