package org.bazaar.productCatalogue.config;

public class StateMachineException extends RuntimeException {
    private String message;

    public StateMachineException(String message) {
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
