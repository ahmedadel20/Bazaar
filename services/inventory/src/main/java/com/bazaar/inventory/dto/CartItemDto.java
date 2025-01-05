package com.bazaar.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record CartItemDto(
                @Positive
                @Schema(requiredProperties = { "Must be positive integer" })
                Long id,
                @Positive
                @Schema(requiredProperties = { "Cannot be null", "Must be positive integer" })
                Long bazaarUserId,
                @Valid
                @Schema(requiredProperties = { "Cannot be null" })
                ProductDto productDto,
                @Positive
                @Schema(requiredProperties = { "Cannot be null", "Must be positive integer" })
                Integer quantity
) {
}
