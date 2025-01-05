package org.bazaar.productCatalogue.client;

import org.bazaar.productCatalogue.sale.dto.PriceUpdateRequest;
import org.bazaar.productCatalogue.sale.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "inventory", url = "${inventory.service.url}")
public interface InventoryClient {
    @PutMapping("/api/v1/products/bycategories")
    List<ProductResponse> getProductsByCategories(List<Long> categories);

    @GetMapping("/api/v1/products/{productId}")
    ProductResponse getSingleProductById(@PathVariable Long productId);

    @PutMapping("/api/v1/products/listofproducts")
    List<ProductResponse> getProductsById(@RequestBody List<Long> productIds);

    @PutMapping("/api/v1/products/updateprices")
    void updateProductPrices(@RequestBody PriceUpdateRequest priceUpdateRequest);
}
