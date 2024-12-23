package com.bazaar.inventory.dto;


import java.sql.Timestamp;

public record ProductDTO (
        Long id,
        Long categoryId,
        String name,
        Double price,
        Long quantity,
        Timestamp lastUpdated
)
{

}
