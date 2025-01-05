package com.bazaar.inventory.dto;

import java.util.List;

public record PriceUpdateRequest(
    List<Long> productIds,
    float discountPercentage
)
{}
