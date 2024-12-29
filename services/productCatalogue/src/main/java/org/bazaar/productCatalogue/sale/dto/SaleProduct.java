package org.bazaar.productCatalogue.sale.dto;

import lombok.Builder;

@Builder
public record SaleProduct(String name,
                Double originalPrice,
                String category) {

}
