package org.bazaar.giza.order.dto;

import lombok.Builder;
import java.util.Date;

@Builder
public record OrderResponse(Long id,
                            Long bazaarUserId,
                            String description,
                            Date orderDate) {
}
