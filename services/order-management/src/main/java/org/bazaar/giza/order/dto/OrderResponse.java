package org.bazaar.giza.order.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.Date;

@Builder
public record OrderResponse(Long id,
                            Long bazaarUserId,
                            String description,
                            BigDecimal finalPrice,
                            Date orderDate) {
}
