package org.bazaar.giza.transaction.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record TransactionRequest(Long id,
                                 Long orderId,
                                 String paymentStatus) {
}