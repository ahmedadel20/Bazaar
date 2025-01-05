package org.bazaar.giza.transaction.dto;

import org.bazaar.giza.constant.ValidationMessage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record TransactionRequest(
                @Positive(message = "id" + ValidationMessage.POSITIVE) Long id,
                @Positive(message = "orderId" + ValidationMessage.POSITIVE) Long orderId,
                @NotBlank(message = "paymentStatus" + ValidationMessage.NOT_BLANK) String paymentStatus) {
}