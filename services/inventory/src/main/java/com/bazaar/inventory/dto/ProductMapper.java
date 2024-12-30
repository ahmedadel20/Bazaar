package com.bazaar.inventory.dto;

import com.bazaar.inventory.entity.Product;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProductMapper {
    CategoryMapper categoryMapper;

    public Product toProduct(ProductDto productDto) {
        return Product
                .builder()
                .id(productDto.id())
                .productCategory(categoryMapper.toCategory(productDto.categoryDto()))
                .name(productDto.name())
                .originalPrice(productDto.originalPrice())
                .currentPrice(productDto.currentPrice())
                .quantity(productDto.quantity())
                .lastUpdated(productDto.lastUpdated())
                .build();
    }

    public ProductDto toProductDTO(Product product) {
        return ProductDto
                .builder()
                .id(product.getId())
                .categoryDto(categoryMapper.toCategoryDTO(product.getProductCategory()))
                .name(product.getName())
                .originalPrice(product.getOriginalPrice())
                .currentPrice(product.getCurrentPrice())
                .quantity(product.getQuantity())
                .lastUpdated(product.getLastUpdated())
                .build();
    }
}
