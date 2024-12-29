package org.bazaar.productCatalogue.sale.dto;

import java.util.List;

public record PriceUpdateRequest(List<Long> productIds, float discountPercentage) {

}
