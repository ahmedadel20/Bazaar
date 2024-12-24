package com.bazaar.inventory.service;

import com.bazaar.inventory.entity.Category;
import com.bazaar.inventory.entity.Product;
import com.bazaar.inventory.exception.ProductNotFoundException;
import com.bazaar.inventory.repo.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductServiceImp implements ProductService{
    private ProductRepository productRepo;
    private CategoryService categoryService;

    @Autowired
    public ProductServiceImp(ProductRepository productRepo, CategoryService categoryService) {
        this.productRepo = productRepo;
        this.categoryService = categoryService;
    }

    public List<Product> getAll() {
        return productRepo.findAll();
    }

    public Product getById(Long id) {
        var product = productRepo.findById(id);
        if (product.isEmpty())
            throw new ProductNotFoundException("No Product Found with ID %d".formatted(id));
        return product.get();
    }

    @Transactional
    public Product create(Product product) {
        product.setId(null);
        product.setProductCategory(categoryService.getById(product.getProductCategory().getId()));
        return productRepo.save(product);
    }

    @Transactional
    public Product update(Product product) {
        if (productRepo.findById(product.getId()).isEmpty())
            throw new ProductNotFoundException("No Product Found with ID %d".formatted(product.getId()));
        product.setProductCategory(categoryService.getById(product.getProductCategory().getId()));
        return productRepo.save(product);
    }

    @Transactional
    public void delete(Long productId) {
        if (productRepo.findById(productId).isEmpty())
            throw new ProductNotFoundException("No Product Found with ID %d".formatted(productId));
        productRepo.deleteById(productId);
    }

}
