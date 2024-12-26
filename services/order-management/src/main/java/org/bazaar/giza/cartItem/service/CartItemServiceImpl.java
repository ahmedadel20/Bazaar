package org.bazaar.giza.cartItem.service;

import lombok.RequiredArgsConstructor;
import org.bazaar.giza.cartItem.dto.*;
import org.bazaar.giza.cartItem.entity.CartItem;
import org.bazaar.giza.cartItem.exception.CartItemNotFoundException;
import org.bazaar.giza.cartItem.exception.InvalidQuantityException;
import org.bazaar.giza.cartItem.mapper.CartItemMapper;
import org.bazaar.giza.cartItem.repository.CartItemRepository;
import org.bazaar.giza.clients.InventoryClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final InventoryClient inventoryClient;

    @Transactional
    public CartItemResponse addItem(CartItemRequest request) {
        ProductDto productDTO = inventoryClient.getProductById(request.productId());

        // Validate quantity
        if (request.quantity() <= 0) {
            throw new InvalidQuantityException();
        }

        // Find existing item
        var existingItem = cartItemRepository.findByBazaarUserIdAndProductId(
                request.bazaarUserId(), request.productId());

        if (existingItem.isPresent()) {
            // Update quantity if product already exists
            var cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + request.quantity());
            cartItem.setCurrentPrice(productDTO.currentPrice());
            return cartItemMapper.toCartItemResponse(cartItemRepository.save(cartItem));
        } else {
            // Add new item if product does not exist
            var cartItem = cartItemRepository.save(cartItemMapper.toCartItem(request));
            cartItem.setCurrentPrice(productDTO.currentPrice());
            return cartItemMapper.toCartItemResponse(cartItem);
        }
    }

    public String removeItem(Long cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new CartItemNotFoundException(cartItemId); // Handle item not found
        }
        cartItemRepository.deleteById(cartItemId);
        return "Item removed";
    }

    public String clearCart(Long bazaarUserId) {
        cartItemRepository.deleteAllByBazaarUserId(bazaarUserId);
        return "Cart cleared";
    }

    public CartItemDto getItem(Long cartItemId) {
        // Fetch the cart item from the database
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId));

        // Fetch the product (including category) via Feign Client
        ProductDto productDto = inventoryClient.getProductById(cartItem.getProductId());

        // Map cart item entity to response DTO
        CartItemResponse cartItemResponse = cartItemMapper.toCartItemResponse(cartItem);

        // Build and return the final DTO using mapper
        return cartItemMapper.toCartItemDto(cartItemResponse, productDto);
    }

    public List<CartItemResponse> getCart(Long bazaarUserId) {
        return cartItemRepository.findAllByBazaarUserId(bazaarUserId).stream().map(cartItemMapper::toCartItemResponse).toList();
    }

    public CartItemResponse updateItemQuantity(Long cartItemId, int quantity) {
        var cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId));

        cartItem.setQuantity(quantity);
        var updatedItem = cartItemRepository.save(cartItem);
        return cartItemMapper.toCartItemResponse(updatedItem);
    }
}