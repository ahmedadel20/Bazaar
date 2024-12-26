package com.bazaar.inventory.dto;

import com.bazaar.inventory.entity.Product;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProductMapper {
    CategoryMapper categoryMapper;

    public Product toProduct(ProductDTO productDto) {
        return new Product(
                productDto.id(),
                categoryMapper.toCategory(productDto.categoryDto()),
                productDto.name(),
                productDto.price(),
                productDto.quantity(),
                productDto.lastUpdated()
        );
    }

    public ProductDTO toProductDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                categoryMapper.toCategoryDTO(product.getProductCategory()),
                product.getName(),
                product.getPrice(),
                product.getQuantity(),
                product.getLastUpdated()
        );
    }
}