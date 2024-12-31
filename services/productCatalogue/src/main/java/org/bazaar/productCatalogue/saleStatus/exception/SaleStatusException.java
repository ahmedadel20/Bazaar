package org.bazaar.productCatalogue.saleStatus.exception;

public class SaleStatusException extends RuntimeException {
    private String message;

    public SaleStatusException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
