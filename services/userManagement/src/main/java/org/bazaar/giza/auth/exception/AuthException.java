package org.bazaar.giza.auth.exception;

public class AuthException extends RuntimeException {
    private String message;

    public AuthException(String message) {
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
