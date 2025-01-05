package org.bazaar.giza.customer.dto;

import org.bazaar.giza.constant.ValidationMessage;

import jakarta.validation.constraints.NotBlank;

public record AddressRequest(
        @NotBlank(message = "street" + ValidationMessage.NOT_BLANK) String street,
        @NotBlank(message = "city" + ValidationMessage.NOT_BLANK) String city,
        @NotBlank(message = "zipCode" + ValidationMessage.NOT_BLANK) String zipCode) {

}
