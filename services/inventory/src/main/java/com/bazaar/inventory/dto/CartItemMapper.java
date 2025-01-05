package com.bazaar.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import com.bazaar.inventory.entity.CartItem;
import org.springframework.stereotype.Component;

@Component
@Builder
@AllArgsConstructor
public class CartItemMapper {
    ProductMapper productMapper;

    public CartItem toCartItem(CartItemDto request) {
        return CartItem.builder()
                .id(request.id())
                .bazaarUserId(request.bazaarUserId())
                .cartProduct(productMapper.toProduct(request.productDto()))
                .quantity(request.quantity())
                .build();
    }

    public CartItemDto toCartItemDto(CartItem cartItem) {
        return CartItemDto.builder()
                .id(cartItem.getId())
                .bazaarUserId(cartItem.getBazaarUserId())
                .productDto(productMapper.toProductDTO(cartItem.getCartProduct()))
                .quantity(cartItem.getQuantity())
                .build();
    }
}
