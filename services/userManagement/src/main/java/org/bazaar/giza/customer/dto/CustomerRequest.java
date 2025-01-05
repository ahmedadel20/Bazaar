package org.bazaar.giza.customer.dto;

import org.bazaar.giza.constant.ValidationMessage;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CustomerRequest(
        Long id,
        @NotBlank(message = "street" + ValidationMessage.NOT_BLANK) String firstName,
        @NotBlank(message = "street" + ValidationMessage.NOT_BLANK) String lastName,
        @NotBlank(message = "email"
                + ValidationMessage.NOT_BLANK) @Email(message = ValidationMessage.INVALID_EMAIL, regexp = ValidationMessage.EMAIL_REGEX) String email,
        @NotBlank(message = "phoneNum"
                + ValidationMessage.NOT_BLANK) @Pattern(message = ValidationMessage.INVALID_PHONE_NUMBER, regexp = ValidationMessage.PHONE_NUMBER_REGEX) String phoneNumber) {
}
