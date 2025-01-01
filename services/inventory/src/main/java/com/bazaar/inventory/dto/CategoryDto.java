package com.bazaar.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CategoryDto(
        @Min(value=0)
        Long id,
        @NotBlank
        String name
)
{

}
