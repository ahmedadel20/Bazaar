package org.bazaar.giza.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "inventory", url = "${inventory.service.url}", path = "/api/v1")
public interface InventoryClient {
    @GetMapping("/products/{productId}")
    ProductDto getProductById(@PathVariable("productId") Long productId);

    @GetMapping("/cart-items/{userId}")
    ResponseEntity<List<CartItemResponse>> getCart(@PathVariable Long userId);

    @DeleteMapping("/{userId}")
    ResponseEntity<String> clearCart(@PathVariable Long userId);
}
