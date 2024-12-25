package org.bazaar.giza.transaction.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.sql.Date;

@Builder
public record TransactionResponse(Long id,
                                  Long orderId,
                                  String paymentStatus,
                                  BigDecimal finalPrice,
                                  Date createdAt) {
}
