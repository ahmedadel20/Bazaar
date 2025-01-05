package com.bazaar.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Builder
public record ProductDto(
                @Min(value = 1)
                @Schema(requiredProperties = {"Must be positive integer"})
                Long id,
                @Valid
                @Schema(requiredProperties = {"Can't be null"})
                CategoryDto categoryDto,
                @NotBlank
                @Schema(requiredProperties = {"Can't be blank"})
                String name,
                @Positive
                @Schema(requiredProperties = {"Must be positive integer"})
                BigDecimal originalPrice,
                @Positive
                @Schema(requiredProperties = {"Must be positive integer"})
                BigDecimal currentPrice,
                @Positive
                @Schema(requiredProperties = {"Must be positive integer"})
                Long quantity,
                @PastOrPresent
                @Schema(requiredProperties = {"Can't be in the future"})
                Timestamp lastUpdated) {

}
