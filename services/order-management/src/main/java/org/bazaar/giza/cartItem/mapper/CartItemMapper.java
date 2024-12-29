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

    public CartItemResponse toCartItemResponse(CartItem cartItem, ProductDto productDto) {
        return CartItemResponse.builder()
                .id(cartItem.getId())
                .bazaarUserId(cartItem.getBazaarUserId())
                .productDto(productDto)
                .quantity(cartItem.getQuantity())
                .currentPrice(cartItem.getCurrentPrice())
                .build();
    }
}
