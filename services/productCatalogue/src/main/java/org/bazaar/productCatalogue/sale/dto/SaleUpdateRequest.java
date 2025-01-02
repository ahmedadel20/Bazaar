package org.bazaar.productCatalogue.sale.dto;

import java.sql.Date;

import org.bazaar.productCatalogue.constant.ValidationMessage;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SaleUpdateRequest(
        @NotNull(message = "id" + ValidationMessage.NOT_NULL) Long id,
        @NotBlank(message = "name" + ValidationMessage.NOT_BLANK) String name,
        @NotNull(message = "discountPercentage" + ValidationMessage.NOT_NULL) @Positive(message = "discountPercentage"
                + ValidationMessage.POSITIVE) @DecimalMax(value = "1.0", message = "discountPercentage"
                        + ValidationMessage.MAX + "1.0", inclusive = false) Float discountPercentage,
        @NotNull(message = "startDate" + ValidationMessage.NOT_NULL) @Future(message = "startDate"
                + ValidationMessage.FUTURE) Date startDate,
        @NotNull(message = "endDate" + ValidationMessage.NOT_NULL) @Future(message = "endDate"
                + ValidationMessage.FUTURE) Date endDate) {

}
