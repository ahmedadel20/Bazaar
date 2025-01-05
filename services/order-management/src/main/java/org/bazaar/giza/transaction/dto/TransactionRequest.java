package org.bazaar.giza.transaction.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record TransactionRequest(
        @Positive
        @Schema(requiredProperties = {"Must be positive integer"})
        Long id,
        @Positive
        @Schema(requiredProperties = {"Must be positive integer"})
        Long orderId,
        @NotBlank
        @Schema(requiredProperties = {"Can't be blank"})
        String paymentStatus
)
{ }