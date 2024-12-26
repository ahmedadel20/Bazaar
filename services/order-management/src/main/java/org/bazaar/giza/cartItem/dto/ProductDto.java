package org.bazaar.giza.cartItem.dto;

import java.math.BigDecimal;
import java.util.Date;

public record ProductDto(Long id,
                         CategoryDto categoryDto,
                         String name,
                         BigDecimal currentPrice,
                         Long quantity,
                         Date lastUpdated ) {
}
