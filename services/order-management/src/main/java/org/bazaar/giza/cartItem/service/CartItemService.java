package org.bazaar.giza.cartItem.service;

import org.bazaar.giza.cartItem.dto.CartItemDto;
import org.bazaar.giza.cartItem.dto.CartItemRequest;
import org.bazaar.giza.cartItem.dto.CartItemResponse;

import java.util.List;

public interface CartItemService {
    CartItemResponse addItem(CartItemRequest request);
    CartItemDto getItem(Long cartItemId);
    String removeItem(Long cartItemId);
    List<CartItemResponse> getCart(Long bazaarUserId);
    String clearCart(Long bazaarUserId);
}