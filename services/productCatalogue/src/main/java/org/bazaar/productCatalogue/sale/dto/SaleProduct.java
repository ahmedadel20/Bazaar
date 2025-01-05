package org.bazaar.productCatalogue.sale.dto;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record SaleProduct(String name,
                BigDecimal originalPrice,
                String category) {

}
