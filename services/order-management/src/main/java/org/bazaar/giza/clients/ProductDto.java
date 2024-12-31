package org.bazaar.giza.clients;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Builder
public record ProductDto(
        @NotNull Long id,
        CategoryDto categoryDto,
        String name,
        BigDecimal originalPrice,
        BigDecimal currentPrice,
        Long quantity,
        Timestamp lastUpdated
)
{

}
