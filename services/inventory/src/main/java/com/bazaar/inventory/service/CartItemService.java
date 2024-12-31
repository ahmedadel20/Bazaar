package com.bazaar.inventory.service;

import com.bazaar.inventory.dto.CartItemDto;
import com.bazaar.inventory.entity.CartItem;

import java.util.List;

public interface CartItemService {
    CartItem addItem(CartItem cartItem);
    CartItem getItem(Long cartItemId);
    String removeItem(Long cartItemId);
    List<CartItem> getCart(Long bazaarUserId);
    String clearCart(Long bazaarUserId);
}