package org.bazaar.giza.cartItem.dto;

import lombok.Builder;

@Builder
public record CategoryDto(Long id,
                          String name) {
}
