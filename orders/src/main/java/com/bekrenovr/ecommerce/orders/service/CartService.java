package com.bekrenovr.ecommerce.orders.service;

import com.bekrenovr.ecommerce.common.security.AuthenticationUtil;
import com.bekrenovr.ecommerce.orders.dto.mapper.ItemEntryMapper;
import com.bekrenovr.ecommerce.orders.dto.response.ItemEntryResponse;
import com.bekrenovr.ecommerce.orders.model.entity.Cart;
import com.bekrenovr.ecommerce.orders.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ItemEntryMapper itemEntryMapper;


    public List<ItemEntryResponse> getCartItems() {
        return getOrCreateCart().getItemEntries().stream()
                .map(itemEntryMapper::entityToResponse)
                .toList();
    }

    private Cart getOrCreateCart() {
        String email = AuthenticationUtil.getAuthenticatedUser().getUsername();
        return cartRepository.findByCustomerEmail(email)
                .orElseGet(() -> {
                    Cart cart = new Cart(email, List.of());
                    return cartRepository.save(cart);
                });
    }
}
