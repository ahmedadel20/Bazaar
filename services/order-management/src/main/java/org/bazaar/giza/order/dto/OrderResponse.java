package org.bazaar.giza.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Date;

@Builder
public record OrderResponse(
        @Min(value=0) Long id,
        @Min(value=0) Long bazaarUserId,
        @NotBlank String description,
        @Min(value=0) BigDecimal finalPrice,
        @PastOrPresent Date orderDate
)
{ }
