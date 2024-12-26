package org.bazaar.giza.user.exception;

public class BazaarUserException extends RuntimeException {
    private String message;

    public BazaarUserException(String message) {
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
