package com.bazaar.inventory.dto;

import com.bazaar.inventory.entity.Category;
import com.bazaar.inventory.entity.Product;
import com.bazaar.inventory.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class ProductMapper {

    public Product toProduct(ProductDTO productDto) {
        return new Product(
                productDto.id(),
                new Category(productDto.categoryId(), "UNKNOWN"),
                productDto.name(),
                productDto.price(),
                productDto.quantity(),
                productDto.lastUpdated()
        );
    }

    public ProductDTO toDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getProductCategory().getId(),
                product.getName(),
                product.getPrice(),
                product.getQuantity(),
                product.getLastUpdated()
        );
    }
}
