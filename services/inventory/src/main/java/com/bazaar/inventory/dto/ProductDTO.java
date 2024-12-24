package com.bazaar.inventory.dto;


import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;

public record ProductDTO (
        @NotNull Long id,
        CategoryDTO categoryDto,
        String name,
        Double price,
        Long quantity,
        Timestamp lastUpdated
)
{

}
