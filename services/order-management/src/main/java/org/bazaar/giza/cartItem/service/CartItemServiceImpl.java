package org.bazaar.giza.cartItem.service;

import lombok.RequiredArgsConstructor;
import org.bazaar.giza.cartItem.dto.CartItemRequest;
import org.bazaar.giza.cartItem.dto.CartItemResponse;
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

        var cartItem = cartItemRepository.save(cartItemMapper.toCartItem(request));
        return cartItemMapper.toCartItemResponse(cartItem);
    }

    @Transactional
    public String removeItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId); //add custom exception
        return "Item removed";
    }

    public CartItemResponse getItem(Long cartItemId) {
        return cartItemMapper.toCartItemResponse(cartItemRepository.findById(cartItemId).orElseThrow()); //add custom exception
    }

    public String clearCart(Long bazaarUserId) {
        cartItemRepository.deleteAllByBazaarUserId(bazaarUserId);
        return "Cart cleared";
    }

    public List<CartItemResponse> getCart(Long bazaarUserId) {
        return cartItemRepository.findAllByBazaarUserId(bazaarUserId).stream().map(cartItemMapper::toCartItemResponse).toList();
    }
}
