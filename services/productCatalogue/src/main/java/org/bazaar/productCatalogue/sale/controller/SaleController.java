package org.bazaar.productCatalogue.sale.controller;

import java.util.List;

import org.bazaar.productCatalogue.sale.dto.SaleCreateRequest;
import org.bazaar.productCatalogue.sale.dto.SaleResponse;
import org.bazaar.productCatalogue.sale.service.SaleService;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/sale")
public class SaleController {
    private final SaleService saleService;

    @PostMapping()
    public SaleResponse createSale(@RequestBody SaleCreateRequest saleCreateRequest) {
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

    @DeleteMapping("/{id}")
    public String deleteSale(@PathVariable Long id) {
        return saleService.deleteSale(id);
    }

}
