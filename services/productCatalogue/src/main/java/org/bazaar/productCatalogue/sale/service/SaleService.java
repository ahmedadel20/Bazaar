package org.bazaar.productCatalogue.sale.service;

import java.util.List;

import org.bazaar.productCatalogue.sale.dto.SaleCreateRequest;
import org.bazaar.productCatalogue.sale.dto.SaleResponse;
import org.bazaar.productCatalogue.sale.dto.SaleUpdateRequest;

public interface SaleService {
    SaleResponse createSale(SaleCreateRequest saleCreateRequest);

    SaleResponse getSingleSale(Long id);

    List<SaleResponse> getAllSales();

    SaleResponse updateSaleDetails(SaleUpdateRequest saleUpdateRequest);

    void activateSales();

    void deactivateSales();

    String deleteSale(Long id);
}
