package org.bazaar.giza.exception;

public class InventoryNotAvailableException extends RuntimeException {
    public InventoryNotAvailableException() {
        super("Inventory Service Not Available");
    }
}
