package org.bazaar.giza.cartItem.dto;

import java.math.BigDecimal;

public record CartItemRequest(Long id,
                              Long bazaarUserId,
                              Long productId,
                              Integer quantity,
                              BigDecimal currentPrice) {
}
