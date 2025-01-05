package org.bazaar.productCatalogue.sale.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
public record ProductResponse(Long id,
        CategoryDto categoryDto,
        String name,
        BigDecimal originalPrice,
        BigDecimal currentPrice,
        Long quantity,
        Timestamp lastUpdated) {

}
