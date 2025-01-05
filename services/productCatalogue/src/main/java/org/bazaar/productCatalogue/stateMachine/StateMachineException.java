package org.bazaar.productCatalogue.stateMachine;

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
