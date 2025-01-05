package org.bazaar.giza.order.dto;

import org.bazaar.giza.constant.ValidationMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record OrderRequest(
        @Positive(message = "id" + ValidationMessage.POSITIVE)
        @Schema(requiredProperties = {"Must be positive integer"})
        Long id,
        
        @Positive(message = "bazaarUserId" + ValidationMessage.POSITIVE)
        @Schema(requiredProperties = {"Must be positive integer"})
        Long bazaarUserId,
        
        @NotBlank(message = "description" + ValidationMessage.NOT_BLANK)
        @Schema(requiredProperties = {"Can't be blank"})
        String description
)
{}
