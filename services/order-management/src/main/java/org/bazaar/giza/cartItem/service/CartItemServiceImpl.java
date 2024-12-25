package org.bazaar.giza.cartItem.service;

import lombok.RequiredArgsConstructor;
import org.bazaar.giza.cartItem.dto.CartItemRequest;
import org.bazaar.giza.cartItem.dto.CartItemResponse;
import org.bazaar.giza.cartItem.exception.CartItemNotFoundException;
import org.bazaar.giza.cartItem.exception.InvalidQuantityException;
import org.bazaar.giza.cartItem.mapper.CartItemMapper;
import org.bazaar.giza.cartItem.repository.CartItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    //private final InventoryClient inventoryClient;

    @Transactional
    public CartItemResponse addItem(CartItemRequest request) {
        //ProductResponse productResponse = inventoryClient.getProductById(request.productId());

        //if (productResponse == null) { // Or check status
        //    throw new IllegalArgumentException("Product not found.");  //add custom exception
        //}

        // Validate quantity
        if (request.quantity() <= 0) {
            throw new InvalidQuantityException("Quantity must be greater than zero.");
        }

        // Find existing item
        var existingItem = cartItemRepository.findByBazaarUserIdAndProductId(
                request.bazaarUserId(), request.productId());

        if (existingItem.isPresent()) {
            // Update quantity if product already exists
            var cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + request.quantity());
            return cartItemMapper.toCartItemResponse(cartItemRepository.save(cartItem));
        } else {
            // Add new item if product does not exist
            var cartItem = cartItemRepository.save(cartItemMapper.toCartItem(request));
            return cartItemMapper.toCartItemResponse(cartItem);
        }
    }

    @Transactional
    public String removeItem(Long cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new CartItemNotFoundException(cartItemId); // Handle item not found
        }
        cartItemRepository.deleteById(cartItemId);
        return "Item removed";
    }

    @Transactional
    public String clearCart(Long bazaarUserId) {
        cartItemRepository.deleteAllByBazaarUserId(bazaarUserId);
        return "Cart cleared";
    }

    public CartItemResponse getItem(Long cartItemId) {
        return cartItemMapper.toCartItemResponse(cartItemRepository.findById(cartItemId).orElseThrow(() -> new CartItemNotFoundException(cartItemId)));
    }

    public List<CartItemResponse> getCart(Long bazaarUserId) {
        return cartItemRepository.findAllByBazaarUserId(bazaarUserId).stream().map(cartItemMapper::toCartItemResponse).toList();
    }

    @Transactional
    public CartItemResponse updateItemQuantity(Long cartItemId, int quantity) {
        var cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId));

        cartItem.setQuantity(quantity);
        var updatedItem = cartItemRepository.save(cartItem);
        return cartItemMapper.toCartItemResponse(updatedItem);
    }
}
