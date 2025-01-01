package com.bazaar.inventory.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CartItemDto(
        @Min(value=0)
        Long id,
        @Min(value=0)
        Long bazaarUserId,
        @Valid
        ProductDto productDto,
        @Min(value=1)
        Integer quantity
) {
}
