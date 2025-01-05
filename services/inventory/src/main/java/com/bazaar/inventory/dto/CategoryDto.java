package com.bazaar.inventory.dto;

import com.bazaar.inventory.constant.ValidationMessage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record CategoryDto(
        @Positive(message = "id" + ValidationMessage.POSITIVE) Long id,
        @NotBlank(message = "name" + ValidationMessage.NOT_BLANK) String name) {

}
