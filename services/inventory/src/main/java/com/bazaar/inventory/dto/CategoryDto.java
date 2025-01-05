package com.bazaar.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record CategoryDto(
                @Positive
                @Schema(requiredProperties = {"Must be positive integer"})
                Long id,
                @NotBlank
                @Schema(requiredProperties = {"Must be unique and not blank"})
                String name) {

}
