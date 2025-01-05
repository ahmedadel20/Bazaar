package com.bazaar.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
                @Positive(message = "id" + ValidationMessage.POSITIVE)
                @Schema(requiredProperties = {"Must be positive integer"})
                Long id,

                @Valid
                @Schema(requiredProperties = {"Can't be null"})
                CategoryDto categoryDto,

                @NotBlank(message = "name" + ValidationMessage.NOT_BLANK)
                @Schema(requiredProperties = {"Can't be blank"})
                String name,

                @Positive(message = "originalPrice" + ValidationMessage.POSITIVE)
                @Schema(requiredProperties = {"Must be positive integer"})
                BigDecimal originalPrice,
                
                @Positive(message = "currentPrice" + ValidationMessage.POSITIVE)
                @Schema(requiredProperties = {"Must be positive integer"})
                BigDecimal currentPrice,

                @Positive(message = "quantity" + ValidationMessage.POSITIVE)
                @Schema(requiredProperties = {"Must be positive integer"})
                Long quantity,
                
                @PastOrPresent(message = "lastUpdated" + ValidationMessage.PAST_OR_PRESENT)
                @Schema(requiredProperties = {"Can't be in the future"})
                Timestamp lastUpdated) {
}
