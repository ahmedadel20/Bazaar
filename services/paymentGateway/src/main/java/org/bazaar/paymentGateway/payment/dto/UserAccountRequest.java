package org.bazaar.paymentGateway.payment.dto;

import java.math.BigDecimal;

import org.bazaar.paymentGateway.constant.ValidationMessage;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UserAccountRequest(
        @NotBlank(message = "email" + ValidationMessage.NOT_BLANK)
        @Email(message = ValidationMessage.INVALID_EMAIL, regexp = ValidationMessage.EMAIL_REGEX)
        @Schema(requiredProperties = {"Can't be blank", "Must be a valid email"})
        String email,
        
        @NotBlank(message = "password" + ValidationMessage.NOT_BLANK)
        @Schema(requiredProperties = {"Can't be blank"})
        String password,
        
        @NotNull(message = "amountOfMoney" + ValidationMessage.NOT_NULL)
        @Positive(message = "amountOfMoney" + ValidationMessage.POSITIVE)
        @Schema(requiredProperties = {"Can't be null", "Must be positive integer"})
        BigDecimal amountOfMoney
)
{}
