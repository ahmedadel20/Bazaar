package com.bazaar.inventory.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Builder
public record ProductDto(
                @Min(value = 0) Long id,
                @Valid CategoryDto categoryDto,
                @NotBlank String name,
                @Min(value = 0) BigDecimal originalPrice,
                @Min(value = 0) BigDecimal currentPrice,
                @Min(value = 0) Long quantity,
                @PastOrPresent Timestamp lastUpdated) {

}
