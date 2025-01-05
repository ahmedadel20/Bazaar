package org.bazaar.giza.transaction.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record TransactionRequest(
        @Min(value=0) Long id,
        @Min(value=0) Long orderId,
        @NotBlank String paymentStatus
)
{ }