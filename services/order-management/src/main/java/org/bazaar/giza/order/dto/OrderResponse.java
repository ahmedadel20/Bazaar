package org.bazaar.giza.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;

@Builder
public record OrderResponse(
        @Positive
        @Schema(requiredProperties = {"Must be positive integer"})
        Long id,
        @Positive
        @Schema(requiredProperties = {"Must be positive integer"})
        Long bazaarUserId,
        @NotBlank
        @Schema(requiredProperties = {"Can't be blank"})
        String description,
        @Positive
        @Schema(requiredProperties = {"Must be positive integer"})
        BigDecimal finalPrice,
        @PastOrPresent
        @Schema(requiredProperties = {"Can't be in the future"})
        Date orderDate
)
{ }
