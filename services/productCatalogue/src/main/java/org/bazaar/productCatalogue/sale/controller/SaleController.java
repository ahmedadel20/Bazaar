package org.bazaar.productCatalogue.sale.controller;

import java.util.List;

import org.bazaar.productCatalogue.sale.dto.SaleCreateRequest;
import org.bazaar.productCatalogue.sale.dto.SaleResponse;
import org.bazaar.productCatalogue.sale.dto.SaleUpdateRequest;
import org.bazaar.productCatalogue.sale.entity.Sale;
import org.bazaar.productCatalogue.sale.service.SaleService;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/sale")
public class SaleController {
    private final SaleService saleService;

    @PostMapping()
    public SaleResponse createSale(@Valid @RequestBody SaleCreateRequest saleCreateRequest) {
        return saleService.createSale(saleCreateRequest);
    }

    @GetMapping("/{id}")
    public SaleResponse getSingleSale(@PathVariable Long id) {
        return saleService.getSingleSale(id);
    }

    @GetMapping()
    public List<SaleResponse> getAllSales() {
        return saleService.getAllSales();
    }

    @PutMapping()
    public SaleResponse updateSale(@PathVariable String id, @Valid @RequestBody SaleUpdateRequest saleUpdateRequest) {
        return saleService.updateSaleDetails(saleUpdateRequest);
    }

    @DeleteMapping("/{id}")
    public String deleteSale(@PathVariable Long id) {
        return saleService.deleteSale(id);
    }

    // FIXME: Remove before production
    @PutMapping("/test/activate/{id}")
    public Sale testActivate(@PathVariable Long id) {
        return saleService.testActivate(id);
    }

    // FIXME: Remove before production
    @PutMapping("/test/deactivate/{id}")
    public Sale testDeactivate(@PathVariable Long id) {
        return saleService.testDeactivate(id);
    }

}
