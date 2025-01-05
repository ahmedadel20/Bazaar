package com.bazaar.inventory.dto;

import com.bazaar.inventory.constant.ValidationMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record CategoryDto(
                @Positive(message = "id" + ValidationMessage.POSITIVE)
                @Schema(requiredProperties = {"Must be positive integer"})
                Long id,
                @NotBlank(message = "name" + ValidationMessage.NOT_BLANK)
                @Schema(requiredProperties = {"Must be unique and not blank"})
                String name) {
}
