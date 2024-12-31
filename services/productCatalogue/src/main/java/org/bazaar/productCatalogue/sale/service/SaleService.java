package org.bazaar.productCatalogue.sale.service;

import java.util.List;

import org.bazaar.productCatalogue.sale.dto.SaleCreateRequest;
import org.bazaar.productCatalogue.sale.dto.SaleResponse;
import org.bazaar.productCatalogue.sale.entity.Sale;

public interface SaleService {
    SaleResponse createSale(SaleCreateRequest saleCreateRequest);

    SaleResponse getSingleSale(Long id);

    List<SaleResponse> getAllSales();

    // SaleResponse updateSaleDetails(SaleUpdateRequest saleUpdateRequest);

    void activateSales();

    void deactivateSales();

    // FIXME: Remove before production
    Sale testActivate(Long id);

    // FIXME: Remove before production
    Sale testDeactivate(Long id);

    String deleteSale(Long id);
}
