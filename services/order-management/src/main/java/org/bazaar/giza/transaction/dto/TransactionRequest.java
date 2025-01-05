package org.bazaar.giza.transaction.dto;

import org.bazaar.giza.constant.ValidationMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record TransactionRequest(
        @Positive(message = "id" + ValidationMessage.POSITIVE)
        @Schema(requiredProperties = {"Must be positive integer"})
        Long id,
  
        @Positive(message = "orderId" + ValidationMessage.POSITIVE)
        @Schema(requiredProperties = {"Must be positive integer"})
        Long orderId,
  
        @NotBlank(message = "paymentStatus" + ValidationMessage.NOT_BLANK)
        @Schema(requiredProperties = {"Can't be blank"})
        String paymentStatus
)
{ }
