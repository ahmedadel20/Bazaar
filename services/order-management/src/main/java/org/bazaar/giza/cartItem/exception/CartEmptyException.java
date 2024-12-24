package org.bazaar.giza.cartItem.exception;

public class CartEmptyException extends RuntimeException {
    public CartEmptyException(Long bazaarUserId) {
        super("Cart is already empty for user ID: " + bazaarUserId);
    }
}
