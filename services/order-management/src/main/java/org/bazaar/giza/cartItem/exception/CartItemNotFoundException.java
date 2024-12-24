package org.bazaar.giza.cartItem.exception;

public class CartItemNotFoundException extends RuntimeException {
    public CartItemNotFoundException(Long cartItemId) {
        super("Cart item with ID " + cartItemId + " not found.");
    }
}
