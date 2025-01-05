package org.bazaar.productCatalogue.sale.dto;

import java.sql.Date;
import java.util.List;

import org.bazaar.productCatalogue.constant.ValidationMessage;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SaleCreateRequest(
        @NotBlank(message = "name" + ValidationMessage.NOT_BLANK) String name,
        @NotNull(message = "discountPercentage" + ValidationMessage.NOT_NULL)
        @Positive(message = "discountPercentage" + ValidationMessage.POSITIVE)
        @DecimalMax(value = "1.0", message = "discountPercentage" + ValidationMessage.MAX + "1.0", inclusive = false)
        @Schema(requiredProperties = {"Must be positive decimal less than 1.0"})
        Float discountPercentage,
        
        @NotNull(message = "startDate" + ValidationMessage.NOT_NULL)
        @Future(message = "startDate" + ValidationMessage.FUTURE)
        @Schema(requiredProperties = {"Can't be in the past"})
        Date startDate,
        
        @NotNull(message = "endDate" + ValidationMessage.NOT_NULL)
        @Future(message = "endDate" + ValidationMessage.FUTURE)
        @Schema(requiredProperties = {"Can't be in the past"})
        Date endDate,
        
        @NotEmpty(message = "categoryIds" + ValidationMessage.NOT_BLANK) 
        @Schema(requiredProperties = {"Can't be empty"})
        List<Long> categoryIds
)
{}