package org.bazaar.giza.cartItem.service;

import lombok.RequiredArgsConstructor;
import org.bazaar.giza.cartItem.dto.CartItemRequest;
import org.bazaar.giza.cartItem.dto.CartItemResponse;
import org.bazaar.giza.cartItem.dto.ProductDto;
import org.bazaar.giza.cartItem.entity.CartItem;
import org.bazaar.giza.cartItem.exception.CartItemNotFoundException;
import org.bazaar.giza.cartItem.exception.InvalidQuantityException;
import org.bazaar.giza.cartItem.mapper.CartItemMapper;
import org.bazaar.giza.cartItem.repository.CartItemRepository;
import org.bazaar.giza.clients.InventoryClient;
import org.bazaar.giza.transaction.dto.NotificationDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final InventoryClient inventoryClient;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public CartItemResponse addItem(CartItemRequest request) {
        ProductDto productDto = inventoryClient.getProductById(request.productId());

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
            cartItem.setCurrentPrice(productDto.currentPrice());
            System.out.println("Product DTO Price: " + productDto.currentPrice());
            return cartItemMapper.toCartItemResponse(cartItemRepository.save(cartItem), productDto);
        } else {
            // Add new item if product does not exist
            var cartItem = cartItemRepository.save(cartItemMapper.toCartItem(request));
            cartItem.setCurrentPrice(productDto.currentPrice());
            System.out.println("CartItem: " + cartItem.getCurrentPrice());
            System.out.println("Product DTO Price: " + productDto.currentPrice());
            return cartItemMapper.toCartItemResponse(cartItem, productDto);
        }
    }

    public String removeItem(Long cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new CartItemNotFoundException(cartItemId); // Handle item not found
        }
        cartItemRepository.deleteById(cartItemId);
        return "Item removed";
    }
    public CartItemResponse updateItem(Long cartItemId, CartItemRequest request) {
        var cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId));

        cartItem.setQuantity(request.quantity());

        ProductDto productDto = inventoryClient.getProductById(request.productId());

        var updatedItem = cartItemRepository.save(cartItem);


        NotificationDto notificationDto = NotificationDto.builder()

                //TODO: get email from jwt token
                .recipient("actualUser@gmail.com") // Replace with actual user email
                .subject(productDto.name() + " has been updated in your cart")
                .body("Your cart has been updated with product: " + productDto.name())
                .sentAt(Instant.now())
                .build();

        rabbitTemplate.convertAndSend("notification_exchange","cart.routing.key", notificationDto);

        return cartItemMapper.toCartItemResponse(updatedItem, productDto);
    }

    public String clearCart(Long bazaarUserId) {
        cartItemRepository.deleteAllByBazaarUserId(bazaarUserId);
        return "Cart cleared";
    }

    public CartItemResponse getItem(Long cartItemId) {
        // Fetch the cart item from the database
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId));

        // Fetch the product (including category) via Feign Client
        ProductDto productDto = inventoryClient.getProductById(cartItem.getProductId());

        // Build and return the final DTO using mapper
        return cartItemMapper.toCartItemResponse(cartItem, productDto);
    }

    public List<CartItemResponse> getCart(Long bazaarUserId) {
        // Fetch all cart items for the user
        List<CartItem> cartItems = cartItemRepository.findAllByBazaarUserId(bazaarUserId);

        // Map each cart item to a response with product details
        return cartItems.stream()
                .map(cartItem -> {
                    // Fetch product details using Feign Client
                    ProductDto productDto = inventoryClient.getProductById(cartItem.getProductId());

                    // Map to response DTO
                    return cartItemMapper.toCartItemResponse(cartItem, productDto);
                })
                .toList(); // Collect as a list
    }

    public CartItemResponse updateItemQuantity(Long cartItemId, int quantity) {
        var cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId));

        cartItem.setQuantity(quantity);
        ProductDto productDto = inventoryClient.getProductById(cartItem.getProductId());
        var updatedItem = cartItemRepository.save(cartItem);
        return cartItemMapper.toCartItemResponse(updatedItem, productDto);
    }
}