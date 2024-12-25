package org.bazaar.paymentGateway.payment.dto;

public record PaymentRequest(String email, String password, float amountOfMoney) {

}
