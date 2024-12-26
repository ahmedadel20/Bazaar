package org.bazaar.giza.order.dto;

import lombok.Builder;

@Builder
public record OrderRequest(Long id,
                           Long bazaarUserId,
                           String description) {
}
