package org.bazaar.productCatalogue.sale.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bazaar.productCatalogue.sale.dto.SaleMapper;
import org.bazaar.productCatalogue.client.InventoryClient;
import org.bazaar.productCatalogue.constant.ErrorMessage;
import org.bazaar.productCatalogue.enums.SaleStatusEnum;
import org.bazaar.productCatalogue.sale.dto.PriceUpdateRequest;
import org.bazaar.productCatalogue.sale.dto.ProductResponse;
import org.bazaar.productCatalogue.sale.dto.SaleCreateRequest;
import org.bazaar.productCatalogue.sale.dto.SaleResponse;
import org.bazaar.productCatalogue.sale.entity.Sale;
import org.bazaar.productCatalogue.sale.exception.SaleException;
import org.bazaar.productCatalogue.sale.repo.SaleRepo;
import org.bazaar.productCatalogue.saleStatus.service.SaleStatusService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class SaleServiceImpl implements SaleService {
    private final SaleRepo repo;
    private final SaleStatusService saleStatusService;
    private final SaleMapper mapper;
    private final InventoryClient inventoryClient;

    @Override
    public SaleResponse createSale(SaleCreateRequest saleCreateRequest) {
        Sale sale = mapper.toSale(saleCreateRequest);
        sale.setId(null);

        /*
         * Send request to InventoryService to retrieve a list of product id's
         * associated with the categories given in sale request
         */
        // FIXME: Have this end point also return the product DTOs
        sale.setProductIds(inventoryClient.getProductsByCategories(saleCreateRequest.categoryIds()));
        List<ProductResponse> productDtos = inventoryClient.getProductsById(sale.getProductIds());

        return mapper.toSaleResponse(repo.save(sale), productDtos);
    }

    @Override
    public SaleResponse getSingleSale(Long id) {
        Sale sale = searchId(id);
        List<ProductResponse> productDtos = inventoryClient.getProductsById(sale.getProductIds());
        return mapper.toSaleResponse(sale, productDtos);
    }

    @Override
    public List<SaleResponse> getAllSales() {
        List<SaleResponse> saleResponses = new ArrayList<>();
        List<Sale> sales = repo.findAll();

        for (Sale sale : sales) {
            List<ProductResponse> productDtos = inventoryClient.getProductsById(sale.getProductIds());
            saleResponses.add(mapper.toSaleResponse(sale, productDtos));
        }

        return saleResponses;
    }

    // @Override
    // public SaleResponse updateSaleDetails(SaleUpdateRequest saleUpdateRequest) {
    // Sale sale = searchId(saleUpdateRequest.id());
    // sale = mapper.toSale(saleUpdateRequest);

    // return mapper.toSaleResponse(repo.save(sale));
    // }

    @Scheduled(cron = "0 1 0 * * ?") // 12:01 AM daily
    @Override
    public void activateSales() {
        Date currentDate = Date.valueOf(LocalDate.now());
        List<Sale> salesStartingToday = repo.findByStartDate(currentDate);

        for (Sale sale : salesStartingToday) {
            sale.setStatus(saleStatusService.getSaleStatusFromStatus(SaleStatusEnum.ACTIVE));
            sale = repo.save(sale);
            inventoryClient
                    .updateProductPrices(new PriceUpdateRequest(sale.getProductIds(), sale.getDiscountPercentage()));
        }
    }

    @Scheduled(cron = "0 59 23 * * ?") // 11:59 PM daily
    @Override
    public void deactivateSales() {
        Date currentDate = Date.valueOf(LocalDate.now());
        List<Sale> salesEndingToday = repo.findByEndDate(currentDate);

        for (Sale sale : salesEndingToday) {
            sale.setStatus(saleStatusService.getSaleStatusFromStatus(SaleStatusEnum.INACTIVE));
            sale = repo.save(sale);
            // 100% percentage to disable sale
            inventoryClient.updateProductPrices(new PriceUpdateRequest(sale.getProductIds(), 1.0f));
        }
    }

    @Override
    public String deleteSale(Long id) {
        Sale sale = searchId(id);
        repo.delete(sale);

        return "Sale deleted successfully.";
    }

    // Helper Functions
    private Sale searchId(Long id) {
        if (id == null) {
            throw new SaleException(ErrorMessage.ID_CANNOT_BE_NULL);
        }

        Optional<Sale> saleOptional = repo.findById(id);
        if (saleOptional.isEmpty()) {
            throw new SaleException(ErrorMessage.SALE_ID_NOT_FOUND);
        }
        return saleOptional.get();
    }
}
