package org.bazaar.giza.order.exception;

public class DuplicateOrderException extends RuntimeException{
    public DuplicateOrderException(Long orderId)
    {
        super("Order with ID: " + orderId);
    }
}
