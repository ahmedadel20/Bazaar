package com.bazaar.inventory.dto;

import jakarta.validation.constraints.NotNull;

public record CategoryDTO (
    @NotNull Long id,
    String name
)
{

}
