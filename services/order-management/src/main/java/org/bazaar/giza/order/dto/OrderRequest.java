package org.bazaar.giza.order.dto;

import org.bazaar.giza.constant.ValidationMessage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record OrderRequest(
                @Positive(message = "id" + ValidationMessage.POSITIVE) Long id,
                @Positive(message = "bazaarUserId" + ValidationMessage.POSITIVE) Long bazaarUserId,
                @NotBlank(message = "description" + ValidationMessage.NOT_BLANK) String description) {
}
