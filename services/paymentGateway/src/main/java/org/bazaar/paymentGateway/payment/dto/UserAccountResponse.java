package org.bazaar.paymentGateway.payment.dto;

import java.math.BigDecimal;

public record UserAccountResponse(String email, BigDecimal moneyInAccount) {

}
