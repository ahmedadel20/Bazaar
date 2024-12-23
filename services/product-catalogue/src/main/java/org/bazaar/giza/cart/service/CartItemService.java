package org.bazaar.giza.cart.service;

import lombok.RequiredArgsConstructor;
import org.bazaar.giza.cart.dto.ProductResponse;
import org.bazaar.giza.cart.mapper.CartItemMapper;
import org.bazaar.giza.cart.dto.CartItemRequest;
import org.bazaar.giza.cart.dto.CartItemResponse;
import org.bazaar.giza.cart.repository.CartItemRepository;
import org.bazaar.giza.clients.InventoryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CartItemService {

    private final CartItemRepository repository;
    private final CartItemMapper mapper;
    //private final InventoryClient inventoryClient;

    @Transactional
    public ResponseEntity<CartItemResponse> addItem(CartItemRequest request) {
        //ProductResponse productResponse = inventoryClient.getProductById(request.productId());

        //ProductResponse productResponse = new ProductResponse(1L, "Razor", BigDecimal.valueOf(10), 1, null);

        //if (productResponse == null) { // Or check status
        //    throw new IllegalArgumentException("Product not found.");  //add custom exception
        //}

        var cartItem = repository.save(mapper.toCartItem(request));
        return ResponseEntity.ok(mapper.toCartItemResponse(cartItem));
    }

    @Transactional
    public ResponseEntity<String> removeItem(Long cartItemId) {
        repository.deleteById(cartItemId); //add custom exception
        return ResponseEntity.ok().body("Item removed");
    }

    public ResponseEntity<CartItemResponse> findById(Long cartItemId) {
        return ResponseEntity.ok(mapper.toCartItemResponse(repository.findById(cartItemId).orElseThrow())); //add custom exception
    }

    public ResponseEntity<String> clearCart(Long bazaarUserId) {
        repository.deleteAllByBazaarUserId(bazaarUserId);
        return ResponseEntity.ok().body("Cart cleared");
    }

    public List<CartItemResponse> getCart(Long bazaarUserId) {
        return repository.findAllByBazaarUserId(bazaarUserId).stream().map(mapper::toCartItemResponse).toList();
    }
}
