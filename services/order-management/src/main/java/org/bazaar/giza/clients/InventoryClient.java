package org.bazaar.giza.clients;

import org.bazaar.giza.cartItem.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "inventory", url = "${inventory.service.url}")
public interface InventoryClient {
    @GetMapping("/api/v1/products/{productId}")
    ProductResponse getProductById(@PathVariable("productId") Long productId);
}
