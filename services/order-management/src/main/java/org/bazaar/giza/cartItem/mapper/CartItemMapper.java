package org.bazaar.giza.cartItem.mapper;

import lombok.Builder;
import org.bazaar.giza.cartItem.dto.*;
import org.bazaar.giza.cartItem.entity.CartItem;
import org.springframework.stereotype.Component;

@Component
@Builder
public class CartItemMapper {
    public CartItem toCartItem(CartItemRequest request) {
        return CartItem.builder()
                .id(request.id())
                .bazaarUserId(request.bazaarUserId())
                .productId(request.productId())
                .quantity(request.quantity())
                .currentPrice(request.currentPrice())
                .build();
    }

    public CartItemResponse toCartItemResponse(CartItem cartItem) {
        return CartItemResponse.builder()
                .id(cartItem.getId())
                .bazaarUserId(cartItem.getBazaarUserId())
                .productId(cartItem.getProductId())
                .quantity(cartItem.getQuantity())
                .currentPrice(cartItem.getCurrentPrice())
                .build();
    }

//    public CartItemDto toCartItemDto(CartItemResponse cartItemResponse, ProductDto productDto) {
//        return CartItemDto.builder()
//                .id(cartItemResponse.id())
//                .bazaarUserId(cartItemResponse.bazaarUserId())
//                .productDto(new ProductDto(
//                        productDto.id(),
//                        new CategoryDto(productDto.categoryDto().id(), productDto.categoryDto().name()), // Map category
//                        productDto.name(),
//                        productDto.currentPrice(), // Convert price to BigDecimal
//                        productDto.quantity(),
//                        productDto.lastUpdated()
//                ))
//                .quantity(cartItemResponse.quantity())
//                .currentPrice(cartItemResponse.currentPrice())
//                .build();
//    }
public CartItemDto toCartItemDto(CartItemResponse cartItemResponse, ProductDto productDto) {
    return CartItemDto.builder()
            .id(cartItemResponse.id())
            .bazaarUserId(cartItemResponse.bazaarUserId())
            .productDto(productDto) // Use Feign Client response directly
            .quantity(cartItemResponse.quantity())
            .currentPrice(cartItemResponse.currentPrice())
            .build();
}
}
