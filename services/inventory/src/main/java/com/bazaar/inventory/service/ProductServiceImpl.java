package com.bazaar.inventory.service;

import com.bazaar.inventory.constant.ErrorMessage;
import com.bazaar.inventory.entity.Product;
import com.bazaar.inventory.exception.ProductDuplicateIdException;
import com.bazaar.inventory.exception.ProductNotFoundException;
import com.bazaar.inventory.repo.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService{
    private ProductRepository productRepo;
    private CategoryService categoryService;

    public List<Product> getAll() {
        return productRepo.findAll();
    }

    public Product getById(Long id) {
        var product = productRepo.findById(id);
        if (product.isEmpty())
            throw new ProductNotFoundException(ErrorMessage.PRODUCT_ID_NOT_FOUND);
        return product.get();
    }

    public Product create(Product product) {
        product.setId(null);
        product.setProductCategory(categoryService.getById(product.getProductCategory().getId()));
        Product savedProduct = null;
        try {
            savedProduct = productRepo.save(product);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(e.getMessage());
            throw new ProductDuplicateIdException(ErrorMessage.DUPLICATE_PRODUCT_ID);
        }
        return savedProduct;
    }

    public Product update(Product product) {
        if (productRepo.findById(product.getId()).isEmpty())
            throw new ProductNotFoundException(ErrorMessage.PRODUCT_ID_NOT_FOUND);
        product.setProductCategory(categoryService.getById(product.getProductCategory().getId()));
        return productRepo.save(product);
    }

    public String delete(Long productId) {
        if (productRepo.findById(productId).isEmpty())
            throw new ProductNotFoundException(ErrorMessage.PRODUCT_ID_NOT_FOUND);
        
        productRepo.deleteById(productId);
        return "PRODUCT DELETED";
    }

}
