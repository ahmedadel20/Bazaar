package org.bazaar.productCatalogue.sale.service;

import java.util.List;
import java.util.Optional;

import org.bazaar.productCatalogue.sale.dto.SaleMapper;
import org.bazaar.productCatalogue.constant.ErrorMessage;
import org.bazaar.productCatalogue.sale.dto.SaleCreateRequest;
import org.bazaar.productCatalogue.sale.dto.SaleResponse;
import org.bazaar.productCatalogue.sale.dto.SaleUpdateRequest;
import org.bazaar.productCatalogue.sale.entity.Sale;
import org.bazaar.productCatalogue.sale.exception.SaleException;
import org.bazaar.productCatalogue.sale.repo.SaleRepo;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class SaleServiceImpl implements SaleService {
    private final SaleRepo repo;
    private final SaleMapper mapper;

    @Override
    public SaleResponse createSale(SaleCreateRequest saleCreateRequest) {
        Sale sale = mapper.toSale(saleCreateRequest);
        sale.setId(null);

        /*
         * TODO:
         * Send request to InventoryService to retrieve a list of product id's
         * associated with the categories given in sale request
         */

        return mapper.toSaleResponse(repo.save(sale));
    }

    @Override
    public SaleResponse getSingleSale(Long id) {
        return mapper.toSaleResponse(searchId(id));
    }

    @Override
    public List<SaleResponse> getAllSales() {
        return repo.findAll().stream().map(mapper::toSaleResponse).toList();
    }

    @Override
    public SaleResponse updateSaleDetails(SaleUpdateRequest saleUpdateRequest) {
        Sale sale = searchId(saleUpdateRequest.id());
        sale = mapper.toSale(saleUpdateRequest);

        return mapper.toSaleResponse(repo.save(sale));
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
