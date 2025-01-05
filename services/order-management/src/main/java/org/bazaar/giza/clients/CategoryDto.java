package org.bazaar.giza.clients;

import org.bazaar.giza.constant.ValidationMessage;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CategoryDto(
        @NotNull(message = "id" + ValidationMessage.NOT_NULL) Long id,
        String name) {

}
