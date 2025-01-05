package com.bazaar.inventory.dto;

import com.bazaar.inventory.constant.ValidationMessage;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record CartItemDto(
        @Positive(message = "id" + ValidationMessage.POSITIVE) Long id,
        @Positive(message = "bazaarUserId" + ValidationMessage.POSITIVE) Long bazaarUserId,
        @Valid ProductDto productDto,
        @Positive(message = "quantity" + ValidationMessage.POSITIVE) Integer quantity) {
}
