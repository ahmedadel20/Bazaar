package com.bazaar.inventory.service;

import com.bazaar.inventory.dto.ProductDTO;
import com.bazaar.inventory.dto.ProductMapper;
import com.bazaar.inventory.entity.Product;
import com.bazaar.inventory.exception.ProductNotFoundException;
import com.bazaar.inventory.repo.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {
    private ProductRepository productRepo;
    private ProductMapper productMapper;

    @Autowired
    public ProductService(ProductRepository productRepo, ProductMapper productMapper) {
        this.productRepo = productRepo;
        this.productMapper = productMapper;
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
    public Product create(ProductDTO productDto) {
        Product product = productMapper.toProduct(productDto);
        product.setId(null);
        return productRepo.save(product);
    }

    @Transactional
    public Product update(ProductDTO productDto) {
        if (productRepo.findById(productDto.id()).isEmpty())
            throw new ProductNotFoundException("No Product Found with ID %d".formatted(productDto.id()));
        return productRepo.save(productMapper.toProduct(productDto));
    }

    @Transactional
    public void delete(Long productId) {
        if (productRepo.findById(productId).isEmpty())
            throw new ProductNotFoundException("No Product Found with ID %d".formatted(productId));
        productRepo.deleteById(productId);
    }

}
