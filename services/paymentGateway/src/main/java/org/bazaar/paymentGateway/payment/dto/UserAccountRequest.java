package org.bazaar.paymentGateway.payment.dto;

public record UserAccountRequest(String email, String password, float amountOfMoney) {

}
