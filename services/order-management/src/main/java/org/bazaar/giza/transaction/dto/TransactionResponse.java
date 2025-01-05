package org.bazaar.giza.transaction.dto;

import lombok.Builder;
import org.bazaar.giza.order.dto.OrderResponse;

import java.math.BigDecimal;
import java.util.Date;

@Builder
public record TransactionResponse(Long id,
                                  OrderResponse orderResponse,
                                  String paymentStatus,
                                  BigDecimal finalPrice,
                                  Date createdAt) {
}
