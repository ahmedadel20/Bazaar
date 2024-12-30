package com.bazaar.inventory.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Builder
public record ProductDTO (
        @NotNull Long id,
        CategoryDTO categoryDto,
        String name,
        BigDecimal originalPrice,
        BigDecimal currentPrice,
        Long quantity,
        Timestamp lastUpdated
)
{

}
