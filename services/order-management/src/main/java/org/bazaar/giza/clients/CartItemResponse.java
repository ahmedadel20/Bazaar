package org.bazaar.giza.clients;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CartItemResponse(Long id,
                               Long bazaarUserId,
                               ProductDto productDto,
                               Integer quantity,
                               BigDecimal currentPrice) {
}
