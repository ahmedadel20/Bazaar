package org.bazaar.giza.cartItem.mapper;

import lombok.Builder;
import org.bazaar.giza.cartItem.dto.CartItemRequest;
import org.bazaar.giza.cartItem.dto.CartItemResponse;
import org.bazaar.giza.cartItem.entity.CartItem;
import org.springframework.stereotype.Service;

@Service
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
}
