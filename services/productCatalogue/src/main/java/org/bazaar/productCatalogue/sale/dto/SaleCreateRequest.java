package org.bazaar.productCatalogue.sale.dto;

import java.sql.Date;
import java.util.List;

public record SaleCreateRequest(String name, Date startDate, Date endDate, List<Long> categoryIds) {
}