package org.bazaar.giza.cartItem.controller;

import lombok.RequiredArgsConstructor;
import org.bazaar.giza.cartItem.dto.CartItemRequest;
import org.bazaar.giza.cartItem.dto.CartItemResponse;
import org.bazaar.giza.cartItem.service.CartItemServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart/item")
public class CartItemController {
    private final CartItemServiceImpl cartItemService;

    @PostMapping()
    public ResponseEntity<CartItemResponse> addItem(@RequestBody CartItemRequest request) {
        return new ResponseEntity<>(cartItemService.addItem(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<String> removeItem(@PathVariable Long cartItemId) {
        return new ResponseEntity<>(cartItemService.removeItem(cartItemId), HttpStatus.OK);
    }

    @GetMapping("/{cartItemId}")
    public ResponseEntity<CartItemResponse> findById(@PathVariable Long cartItemId) {
        return new ResponseEntity<>(cartItemService.getItem(cartItemId), HttpStatus.OK);
    }

    @PatchMapping("/{cartItemId}/update-quantity")
    public ResponseEntity<CartItemResponse> updateItemQuantity(
            @PathVariable Long cartItemId,
            @RequestParam int quantity) {
        return ResponseEntity.ok(cartItemService.updateItemQuantity(cartItemId, quantity));
    }

    @PostMapping("/add-or-update")
    public ResponseEntity<CartItemResponse> addOrUpdateItem(
            @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartItemService.addOrUpdateItem(request));
    }

    @GetMapping()
    public ResponseEntity<List<CartItemResponse>> getCart() {
        Long bazaarUserId = getCurrentBazaarUserId(); // Extract userId from JWT
        return new ResponseEntity<>(cartItemService.getCart(bazaarUserId), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> clearCart() {
        Long bazaarUserId = getCurrentBazaarUserId(); // Extract userId from JWT
        return new ResponseEntity<>(cartItemService.clearCart(bazaarUserId), HttpStatus.OK);
    }

    // Method to extract userId from JWT
    private Long getCurrentBazaarUserId() {
        // Implementation as shown earlier
        return null;
    }
}
