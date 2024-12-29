package org.bazaar.productCatalogue.sale.dto;

import org.bazaar.productCatalogue.sale.entity.Sale;
import org.springframework.stereotype.Component;

@Component
public class SaleMapper {
    public Sale toSale(SaleCreateRequest saleCreateRequest) {
        Sale sale = Sale.builder()
                .name(saleCreateRequest.name())
                .discountPercentage(saleCreateRequest.discountPercentage())
                .startDate(saleCreateRequest.startDate())
                .endDate(saleCreateRequest.endDate())
                .build();
        return sale;
    }

    public Sale toSale(SaleUpdateRequest saleUpdateRequest) {
        Sale sale = Sale.builder()
                .id(saleUpdateRequest.id())
                .name(saleUpdateRequest.name())
                .startDate(saleUpdateRequest.startDate())
                .endDate(saleUpdateRequest.endDate())
                .build();
        return sale;
    }

    public SaleResponse toSaleResponse(Sale sale) {
        SaleResponse saleResponse = SaleResponse.builder()
                .id(sale.getId())
                .name(sale.getName())
                .startDate(sale.getStartDate())
                .endDate(sale.getEndDate())
                .status(sale.getStatus())
                .productIds(sale.getProductIds())
                .build();
        return saleResponse;
    }
}
