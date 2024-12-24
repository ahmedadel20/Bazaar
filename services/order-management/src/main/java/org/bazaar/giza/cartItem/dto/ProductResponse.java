package org.bazaar.giza.cartItem.dto;

import java.math.BigDecimal;
import java.util.Date;

public record ProductResponse(Long id,
                              String name,
                              BigDecimal price,
                              Integer quantity,
                              Date updatedAt ) {
}
