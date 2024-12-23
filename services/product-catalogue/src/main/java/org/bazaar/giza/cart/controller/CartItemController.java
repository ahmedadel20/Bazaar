package org.bazaar.giza.cart.controller;

import lombok.RequiredArgsConstructor;
import org.bazaar.giza.cart.dto.CartItemRequest;
import org.bazaar.giza.cart.dto.CartItemResponse;
import org.bazaar.giza.cart.service.CartItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartItemController {
    private final CartItemService service;

    @PostMapping("/item/add")
    public ResponseEntity<CartItemResponse> addItem(@RequestBody CartItemRequest request) {
        return service.addItem(request);
    }

    @DeleteMapping("/item/remove/{cartItemId}")
    public ResponseEntity<String> removeItem(@PathVariable Long cartItemId) {
        return service.removeItem(cartItemId);
    }

    @GetMapping("/item/{cartItemId}")
    public ResponseEntity<CartItemResponse> findById(@PathVariable Long cartItemId) {
        return service.findById(cartItemId);
    }

    @GetMapping()
    public ResponseEntity<List<CartItemResponse>> getCart() {
        Long bazaarUserId = getCurrentBazaarUserId(); // Extract userId from JWT
        return ResponseEntity.ok(service.getCart(bazaarUserId));
    }

    @DeleteMapping
    public ResponseEntity<String> clearCart() {
        Long bazaarUserId = getCurrentBazaarUserId(); // Extract userId from JWT
        return service.clearCart(bazaarUserId);
    }

    // Method to extract userId from JWT
    private Long getCurrentBazaarUserId() {
        // Implementation as shown earlier
        return null;
    }
}