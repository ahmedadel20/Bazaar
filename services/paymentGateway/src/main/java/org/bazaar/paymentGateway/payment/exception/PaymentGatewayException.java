package org.bazaar.paymentGateway.payment.exception;

public class PaymentGatewayException extends RuntimeException {
    private String message;

    public PaymentGatewayException(String message) {
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
