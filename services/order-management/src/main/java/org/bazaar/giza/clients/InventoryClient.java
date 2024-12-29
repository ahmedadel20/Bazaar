package org.bazaar.giza.clients;

import org.bazaar.giza.cartItem.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "inventory", url = "${inventory.service.url}", path = "/api/v1")
public interface InventoryClient {
    @GetMapping("/products/{productId}")
    ProductDto getProductById(@PathVariable("productId") Long productId);
}
