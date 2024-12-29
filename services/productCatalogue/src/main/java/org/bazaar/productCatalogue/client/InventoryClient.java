package org.bazaar.productCatalogue.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "inventory", url = "${inventory.service.url}")
public interface InventoryClient {
    @GetMapping("/api/v1/products/bycategories")
    List<Long> getProductById(List<Long> categories);

    // FIXME: Update url and method name
    @PutMapping("/api/v1/products/updateprices")
    void updateProductPrices(List<Long> productIds, float discountPercentage);
}
