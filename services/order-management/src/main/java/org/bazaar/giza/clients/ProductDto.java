package org.bazaar.giza.clients;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.bazaar.giza.constant.ValidationMessage;

@Builder
public record ProductDto(
                @NotNull(message = "id" + ValidationMessage.NOT_NULL) Long id,
                CategoryDto categoryDto,
                String name,
                BigDecimal originalPrice,
                BigDecimal currentPrice,
                Long quantity,
                Timestamp lastUpdated) {

}
