package com.bekrenovr.ecommerce.customers.wishlist;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import com.bekrenovr.ecommerce.common.security.AuthenticationUtil;
import com.bekrenovr.ecommerce.customers.customer.Customer;
import com.bekrenovr.ecommerce.customers.customer.CustomerRepository;
import com.bekrenovr.ecommerce.customers.proxy.CatalogProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.bekrenovr.ecommerce.customers.exception.CustomersApplicationExceptionReason.ITEM_ALREADY_ON_WISH_LIST;
import static com.bekrenovr.ecommerce.customers.exception.CustomersApplicationExceptionReason.WISH_LIST_ITEM_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class WishListService {
    private final CustomerRepository customerRepository;
    private final CatalogProxy catalogProxy;

    public ResponseEntity<?> getForCustomer() {
        String email = AuthenticationUtil.getAuthenticatedUser().getUsername();
        List<UUID> wishListItemsIds = customerRepository.findByEmailOrThrowDefault(email).getWishListItems();
        return catalogProxy.getItemsByIds(wishListItemsIds);
    }

    public void addItem(UUID itemId) {
        catalogProxy.getItemById(itemId); // check if item exists (if not, 404 exception is thrown)
        String email = AuthenticationUtil.getAuthenticatedUser().getUsername();
        Customer customer = customerRepository.findByEmailOrThrowDefault(email);
        if(customer.getWishListItems().contains(itemId)){
            throw new EcommerceApplicationException(ITEM_ALREADY_ON_WISH_LIST, itemId);
        }
        customer.getWishListItems().add(itemId);
        customerRepository.save(customer);
    }

    public void removeItem(UUID itemId) {
        String email = AuthenticationUtil.getAuthenticatedUser().getUsername();
        Customer customer = customerRepository.findByEmailOrThrowDefault(email);
        if(!customer.getWishListItems().contains(itemId)){
            throw new EcommerceApplicationException(WISH_LIST_ITEM_NOT_FOUND, itemId);
        }
        customer.getWishListItems().remove(itemId);
        customerRepository.save(customer);
    }

    public void clear() {
        String email = AuthenticationUtil.getAuthenticatedUser().getUsername();
        Customer customer = customerRepository.findByEmailOrThrowDefault(email);
        customer.getWishListItems().clear();
        customerRepository.save(customer);
    }
}
