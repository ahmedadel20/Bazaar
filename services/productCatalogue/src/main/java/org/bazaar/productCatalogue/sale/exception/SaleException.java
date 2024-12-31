package org.bazaar.productCatalogue.sale.exception;

public class SaleException extends RuntimeException {
    private String message;

    public SaleException(String message) {
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
