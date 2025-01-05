package org.bazaar.giza.auth.dto;

import jakarta.validation.constraints.NotBlank;
import org.bazaar.giza.constant.ValidationMessage;

public record SignInRequestDTO(
        @NotBlank(message = "email"
                + ValidationMessage.NOT_BLANK) String email,
        @NotBlank(message = "password"
                + ValidationMessage.NOT_BLANK) String password) {

}
