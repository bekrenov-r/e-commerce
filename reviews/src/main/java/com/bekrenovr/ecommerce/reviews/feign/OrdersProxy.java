package com.bekrenovr.ecommerce.reviews.feign;

import com.bekrenovr.ecommerce.common.security.SecurityConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "orders", path = "/orders")
public interface OrdersProxy {
    @GetMapping("/customer")
    ResponseEntity<Page<Map<?, ?>>> getAllOrdersForCustomer(
            @RequestHeader(SecurityConstants.AUTHENTICATED_USER_HEADER) String authenticatedUser,
            @RequestParam String status,
            @RequestParam int pageNumber,
            @RequestParam int pageSize);
}
