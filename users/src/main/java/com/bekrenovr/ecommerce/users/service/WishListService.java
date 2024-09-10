package com.bekrenovr.ecommerce.users.service;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import com.bekrenovr.ecommerce.common.security.AuthenticationUtil;
import com.bekrenovr.ecommerce.users.model.entity.Customer;
import com.bekrenovr.ecommerce.users.proxy.CatalogProxy;
import com.bekrenovr.ecommerce.users.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.bekrenovr.ecommerce.users.exception.UsersApplicationExceptionReason.ITEM_ALREADY_ON_WISH_LIST;
import static com.bekrenovr.ecommerce.users.exception.UsersApplicationExceptionReason.WISH_LIST_ITEM_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class WishListService {
    private final CustomerRepository customerRepository;
    private final CatalogProxy catalogProxy;

    public ResponseEntity<?> getWishListItemsForCustomer() {
        String email = AuthenticationUtil.getAuthenticatedUser().getUsername();
        List<UUID> wishListItemsIds = customerRepository.findByEmailOrThrowDefault(email).getWishListItems();
        return catalogProxy.getItemsByIds(wishListItemsIds);
    }

    public void addItemToWishList(UUID itemId) {
        catalogProxy.getItemById(itemId); // check if item exists (if not, 404 exception is thrown)
        String email = AuthenticationUtil.getAuthenticatedUser().getUsername();
        Customer customer = customerRepository.findByEmailOrThrowDefault(email);
        if(customer.getWishListItems().contains(itemId)){
            throw new EcommerceApplicationException(ITEM_ALREADY_ON_WISH_LIST, itemId);
        }
        customer.getWishListItems().add(itemId);
        customerRepository.save(customer);
    }

    public void removeItemFromWishList(UUID itemId) {
        String email = AuthenticationUtil.getAuthenticatedUser().getUsername();
        Customer customer = customerRepository.findByEmailOrThrowDefault(email);
        if(!customer.getWishListItems().contains(itemId)){
            throw new EcommerceApplicationException(WISH_LIST_ITEM_NOT_FOUND, itemId);
        }
        customer.getWishListItems().remove(itemId);
        customerRepository.save(customer);
    }

    public void clearWishList() {
        String email = AuthenticationUtil.getAuthenticatedUser().getUsername();
        Customer customer = customerRepository.findByEmailOrThrowDefault(email);
        customer.getWishListItems().clear();
        customerRepository.save(customer);
    }
}
