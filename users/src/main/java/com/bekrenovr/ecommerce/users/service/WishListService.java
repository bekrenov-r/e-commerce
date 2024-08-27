package com.bekrenovr.ecommerce.users.service;

import com.bekrenovr.ecommerce.catalog.dto.response.ItemResponse;
import com.bekrenovr.ecommerce.common.auth.AuthenticationUtil;
import com.bekrenovr.ecommerce.users.proxy.CatalogProxy;
import com.bekrenovr.ecommerce.users.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WishListService {
    private final CustomerRepository customerRepository;
    private final CatalogProxy catalogProxy;

    public List<ItemResponse> getWishListItemsForCustomer() {
        String email = AuthenticationUtil.getAuthenticatedUser().getUsername();
        List<UUID> wishListItemsIds = customerRepository.findByEmailOrThrowDefault(email).getWishListItems();
        System.out.println(wishListItemsIds);
        return catalogProxy.getItemsByIds(wishListItemsIds).getBody();
    }
}
