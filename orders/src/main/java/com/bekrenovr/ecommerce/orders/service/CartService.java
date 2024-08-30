package com.bekrenovr.ecommerce.orders.service;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import com.bekrenovr.ecommerce.common.security.AuthenticationUtil;
import com.bekrenovr.ecommerce.orders.dto.mapper.ItemEntryMapper;
import com.bekrenovr.ecommerce.orders.dto.request.ItemEntryRequest;
import com.bekrenovr.ecommerce.orders.dto.response.ItemEntryResponse;
import com.bekrenovr.ecommerce.orders.dto.response.ItemResponse;
import com.bekrenovr.ecommerce.orders.model.entity.Cart;
import com.bekrenovr.ecommerce.orders.model.entity.ItemEntry;
import com.bekrenovr.ecommerce.orders.proxy.CatalogProxy;
import com.bekrenovr.ecommerce.orders.repository.CartRepository;
import com.bekrenovr.ecommerce.orders.validation.ItemEntryValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.bekrenovr.ecommerce.orders.exception.OrdersApplicationExceptionReason.CART_ENTRY_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ItemEntryMapper itemEntryMapper;
    private final CatalogProxy catalogProxy;

    public List<ItemEntryResponse> getCartItems() {
        return getOrCreateCart().getItemEntries().stream()
                .map(itemEntryMapper::entityToResponse)
                .toList();
    }

    public void addItemToCart(ItemEntryRequest request) {
        Cart cart = getOrCreateCart();
        ItemResponse item = catalogProxy.getItemById(request.itemId()).getBody();
        ItemEntryValidator.validateEntryAgainstItem(request, item);
        ItemEntry itemEntry = itemEntryMapper.itemResponseToEntity(item, request.quantity(), request.size());
        cart.getItemEntries().add(itemEntry);
        cartRepository.save(cart);
    }

    public void updateCartItem(UUID itemEntryId, int quantity, String size) {
        Cart cart = getOrCreateCart();
        ItemEntry itemEntry = cart.getItemEntries().stream()
                .filter(entry -> entry.getId().equals(itemEntryId))
                .findFirst()
                .orElseThrow(() -> new EcommerceApplicationException(CART_ENTRY_NOT_FOUND, itemEntryId));
        ItemResponse item = catalogProxy.getItemById(itemEntry.getItemId()).getBody();
        ItemEntryValidator.validateEntryAgainstItem(new ItemEntryRequest(item.id(), quantity, size), item);

        itemEntry.setQuantity(quantity);
        itemEntry.setItemSize(size);
        cartRepository.save(cart);
    }

    public void removeItemFromCart(UUID itemEntryId) {
        Cart cart = getOrCreateCart();
        ItemEntry entryToRemove = cart.getItemEntries().stream()
                .filter(entry -> entry.getId().equals(itemEntryId))
                .findFirst()
                .orElseThrow(() -> new EcommerceApplicationException(CART_ENTRY_NOT_FOUND, itemEntryId));
        cart.getItemEntries().remove(entryToRemove);
        cartRepository.save(cart);
    }

    public void clearCart() {
        Cart cart = getOrCreateCart();
        cart.getItemEntries().clear();
        cartRepository.save(cart);
    }

    private Cart getOrCreateCart() {
        String email = AuthenticationUtil.getAuthenticatedUser().getUsername();
        return cartRepository.findByCustomerEmail(email)
                .orElseGet(() -> {
                    Cart cart = new Cart(email, new ArrayList<>());
                    return cartRepository.save(cart);
                });
    }
}
