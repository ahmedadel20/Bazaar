package org.bazaar.paymentGateway.payment.dto;

import org.bazaar.paymentGateway.constant.ValidationMessage;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record UserAccountRequest(
        @Email(message = ValidationMessage.INVALID_EMAIL, regexp = ValidationMessage.EMAIL_REGEX) String email,
        @NotBlank(message = "password" + ValidationMessage.NOT_BLANK) String password,
        @Positive(message = "amountOfMoney" + ValidationMessage.POSITIVE) float amountOfMoney) {

}
