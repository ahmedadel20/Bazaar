package org.bazaar.productCatalogue.sale.dto;

import java.sql.Date;
import java.util.List;

import org.bazaar.productCatalogue.saleStatus.entity.SaleStatus;

import lombok.Builder;

@Builder
public record SaleResponse(Long id, String name, float discountPercentage, Date startDate, Date endDate,
                SaleStatus status, List<Long> categoryIds, List<SaleProduct> products) {
}
