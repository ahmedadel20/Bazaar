package org.bazaar.productCatalogue.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "inventory", url = "${inventory.service.url}")
public interface InventoryClient {
    @GetMapping("/api/v1/products/bycategories")
    List<Long> getProductById(List<String> categories);
}
