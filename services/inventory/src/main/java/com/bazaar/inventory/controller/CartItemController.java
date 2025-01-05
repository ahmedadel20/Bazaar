package com.bazaar.inventory.controller;

import com.bazaar.inventory.dto.CartItemMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.bazaar.inventory.dto.CartItemDto;
import com.bazaar.inventory.service.CartItemServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart-items")
public class CartItemController {
    private final CartItemServiceImpl cartItemService;
    private final CartItemMapper cartItemMapper;

    @PostMapping
    @ResponseBody
    public CartItemDto addItem(@RequestBody @Valid CartItemDto request) {
        return cartItemMapper.toCartItemDto(cartItemService.addItem(cartItemMapper.toCartItem(request)));
    }

    @DeleteMapping("/{cartItemId}")
    @ResponseBody
    public String removeItem(@PathVariable Long cartItemId) {
        return cartItemService.removeItem(cartItemId);
    }

    @PutMapping
    @ResponseBody
    public CartItemDto updateItem(@RequestBody @Valid CartItemDto request) {
        return cartItemMapper.toCartItemDto(cartItemService.updateItem(cartItemMapper.toCartItem(request)));
    }

    @GetMapping("/{cartItemId}")
    @ResponseBody
    public CartItemDto findById(@PathVariable Long cartItemId) {
        return cartItemMapper.toCartItemDto(cartItemService.getItem(cartItemId));
    }

    @PatchMapping("/{cartItemId}/update-quantity")
    @ResponseBody
    public CartItemDto updateItemQuantity(
            @PathVariable Long cartItemId,
            @RequestParam int quantity) {
        return cartItemMapper.toCartItemDto(cartItemService.updateItemQuantity(cartItemId, quantity));
    }

    @GetMapping("/user-id/{userId}")
    @ResponseBody
    public List<CartItemDto> getCart(@PathVariable Long userId) {
        // Long bazaarUserId = getCurrentBazaarUserId(); // Extract userId from JWT

        // Only for testing
        // Long bazaarUserId = 1L;
        return cartItemService.getCart(userId)
                .stream()
                .map(cartItemMapper::toCartItemDto)
                .toList();
    }

    @DeleteMapping("/user-id/{userId}")
    @ResponseBody
    public String clearCart(@PathVariable Long userId) {
        // Long userId = getCurrentBazaarUserId(); // Extract userId from JWT
        return cartItemService.clearCart(userId);
    }
}