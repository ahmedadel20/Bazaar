package org.bazaar.giza.cartItem.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CartItemResponse(Long id,
                               Long bazaarUserId,
                               Long productId,
                               Integer quantity,
                               BigDecimal currentPrice) {
}
