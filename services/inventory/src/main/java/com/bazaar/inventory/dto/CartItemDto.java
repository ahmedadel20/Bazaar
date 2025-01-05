package com.bazaar.inventory.dto;

import com.bazaar.inventory.constant.ValidationMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record CartItemDto(
                @Positive(message = "id" + ValidationMessage.POSITIVE)
                @Schema(requiredProperties = { "Must be positive integer" })
                Long id,
                @Positive(message = "bazaarUserId" + ValidationMessage.POSITIVE)
                @Schema(requiredProperties = { "Cannot be null", "Must be positive integer" })
                Long bazaarUserId,
                @Valid
                @Schema(requiredProperties = { "Cannot be null" })
                ProductDto productDto,
                @Positive(message = "quantity" + ValidationMessage.POSITIVE)
                @Schema(requiredProperties = { "Cannot be null", "Must be positive integer" })
                Integer quantity
) {
}
