package com.bazaar.inventory.service;

import com.bazaar.inventory.dto.ProductDto;
import com.bazaar.inventory.entity.Product;
import lombok.RequiredArgsConstructor;
import com.bazaar.inventory.dto.CartItemDto;
import com.bazaar.inventory.entity.CartItem;
import com.bazaar.inventory.exception.CartItemNotFoundException;
import com.bazaar.inventory.exception.InvalidQuantityException;
import com.bazaar.inventory.dto.CartItemMapper;
import com.bazaar.inventory.repo.CartItemRepository;
import com.bazaar.inventory.dto.NotificationDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CartItemServiceImpl implements CartItemService {
    @Value("${rabbitmq.exchange.notification}")
    String notificationExchange;
    @Value("${rabbitmq.routing.cart}")
    String cartRoutingKey;

    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final ProductService productService;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public CartItem addItem(CartItem cartItem) {
        Product product = productService.getById(cartItem.getCartProduct().getId());
        cartItem.setCartProduct(product);

        // Find existing item
        List<CartItem> existingItem = cartItemRepository
                .findByBazaarUserIdAndProductId(
                        cartItem.getBazaarUserId(), cartItem.getCartProduct().getId()
                );

        if (!existingItem.isEmpty()) {
            // Update quantity if product already exists
            CartItem existingCartItem = existingItem.get(0);
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItem.getQuantity());
            return cartItemRepository.save(existingCartItem);
        } else {
            // Add new item if product does not exist
            cartItem.setId(null);
            return cartItemRepository.save(cartItem);
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

    public CartItem updateItem(CartItem cartItem) {
        var existingCartItem = cartItemRepository.findById(cartItem.getId())
                .orElseThrow(() -> new CartItemNotFoundException(cartItem.getId()));

        existingCartItem.setQuantity(cartItem.getQuantity());

        Product product = productService.getById(cartItem.getCartProduct().getId());
        cartItem.setCartProduct(product);
        var updatedItem = cartItemRepository.save(existingCartItem);

        NotificationDto notificationDto = NotificationDto.builder()

                //TODO: get email from jwt token
                .recipient("abdalla.maged95@gmail.com") // Replace with actual user email
                .subject(product.getName() + " has been updated in your cart")
                .body("Your cart has been updated with product: " + product.getName())
                .sentAt(Instant.now())
                .build();

        rabbitTemplate.convertAndSend(notificationExchange, cartRoutingKey, notificationDto);

        return updatedItem;
    }

    @Transactional
    public String clearCart(Long bazaarUserId) {
        cartItemRepository.deleteAllByBazaarUserId(bazaarUserId);
        return "Cart cleared";
    }

    public CartItem getItem(Long cartItemId) {
        // Fetch the cart item from the database
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId));

        // Build and return the final DTO using mapper
        return cartItem;
    }

    public List<CartItem> getCart(Long bazaarUserId) {
        // Fetch all cart items for the user
        return cartItemRepository.findAllByBazaarUserId(bazaarUserId);
    }

    public CartItem updateItemQuantity(Long cartItemId, int quantity) {
        var cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId));

        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }
}