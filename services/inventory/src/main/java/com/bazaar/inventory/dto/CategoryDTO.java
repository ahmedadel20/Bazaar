package com.bazaar.inventory.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CategoryDTO (
    @NotNull Long id,
    String name
)
{

}
