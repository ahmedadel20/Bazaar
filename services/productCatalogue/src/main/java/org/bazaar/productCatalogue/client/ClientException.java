package org.bazaar.productCatalogue.client;

public class ClientException extends RuntimeException {
    private String message;

    public ClientException(String message) {
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
