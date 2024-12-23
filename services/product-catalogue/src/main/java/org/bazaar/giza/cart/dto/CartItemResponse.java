package org.bazaar.giza.cart.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CartItemResponse(Long id,
                               Long bazaarUserId,
                               Long productId,
                               Integer quantity,
                               BigDecimal currentPrice) {
}