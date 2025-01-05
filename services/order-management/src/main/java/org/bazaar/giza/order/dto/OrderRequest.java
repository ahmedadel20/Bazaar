package org.bazaar.giza.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record OrderRequest(
        @Positive
        @Schema(requiredProperties = {"Must be positive integer"})
        Long id,
        @Positive
        @Schema(requiredProperties = {"Must be positive integer"})
        Long bazaarUserId,
        @NotBlank
        @Schema(requiredProperties = {"Can't be blank"})
        String description
)
{}
