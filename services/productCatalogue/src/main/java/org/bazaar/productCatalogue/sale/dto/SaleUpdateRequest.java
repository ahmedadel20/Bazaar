package org.bazaar.productCatalogue.sale.dto;

import java.sql.Date;

public record SaleUpdateRequest(Long id, String name, Date startDate, Date endDate) {
    
}
