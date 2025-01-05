package org.bazaar.giza.clients;

import lombok.Builder;

@Builder
public record CartItemResponse(
                Long id,
                Long bazaarUserId,
                ProductDto productDto,
                Integer quantity) {
}
