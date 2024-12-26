package org.bazaar.giza.cartItem.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CartItemDto(Long id,
                          Long bazaarUserId,
                          ProductDto productDto,
                          Integer quantity,
                          BigDecimal currentPrice) {
}
