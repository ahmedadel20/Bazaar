package org.bazaar.giza.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record OrderRequest(
        @Min(value=0)
        Long id,
        @Min(value=0)
        Long bazaarUserId,
        @NotBlank
        String description) {
}
