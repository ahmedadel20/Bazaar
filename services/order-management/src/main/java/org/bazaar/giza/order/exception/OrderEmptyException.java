package org.bazaar.giza.order.exception;

public class OrderEmptyException extends RuntimeException {
    public OrderEmptyException(Long bazaarUserId) {
        super("No orders found for user ID: " + bazaarUserId);
    }
}
