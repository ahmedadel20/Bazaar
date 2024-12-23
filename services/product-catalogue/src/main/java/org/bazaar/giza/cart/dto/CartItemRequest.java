package org.bazaar.giza.cart.dto;

import java.math.BigDecimal;

public record CartItemRequest(Long id,
                             Long bazaarUserId,
                             Long productId,
                             Integer quantity,
                             BigDecimal currentPrice) {
}
