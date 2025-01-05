package com.bazaar.inventory.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.bazaar.inventory.constant.ValidationMessage;

@Builder
public record ProductDto(
        @Positive(message = "id" + ValidationMessage.POSITIVE) Long id,
        @Valid CategoryDto categoryDto,
        @NotBlank(message = "name" + ValidationMessage.NOT_BLANK) String name,
        @Positive(message = "originalPrice" + ValidationMessage.POSITIVE) BigDecimal originalPrice,
        @Positive(message = "currentPrice" + ValidationMessage.POSITIVE) BigDecimal currentPrice,
        @Positive(message = "quantity" + ValidationMessage.POSITIVE) Long quantity,
        @PastOrPresent(message = "lastUpdated" + ValidationMessage.PAST_OR_PRESENT) Timestamp lastUpdated) {

}
