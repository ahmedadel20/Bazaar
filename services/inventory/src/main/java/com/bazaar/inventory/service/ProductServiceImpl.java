package com.bazaar.inventory.service;

import com.bazaar.inventory.constant.ErrorMessage;
import com.bazaar.inventory.entity.Category;
import com.bazaar.inventory.entity.Product;
import com.bazaar.inventory.exception.ProductNotFoundException;
import com.bazaar.inventory.repo.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepo;
    private CategoryService categoryService;

    @Override
    public List<Product> getAll() {
        return productRepo.findAll();
    }

    @Override
    public Product getById(Long id) {
        var product = productRepo.findById(id);
        return product.orElseThrow(() -> new ProductNotFoundException(ErrorMessage.PRODUCT_ID_NOT_FOUND));
    }

    @Override
    public List<Product> getProductsByCategory(Category category) {
        Category existingCategory = categoryService.getById(category.getId());
        return productRepo.findByProductCategory(existingCategory);
    }

    @Override
    public List<Product> getProductsByCategories(List<Long> categoryIds) {
        List<Product> products = new ArrayList<>();
        categoryIds.forEach(
                i -> products.addAll(getProductsByCategory(Category.builder().id(i).build())));
        return products;
    }

    @Override
    public List<Product> getProductsByIds(List<Long> productIds) {
        List<Product> products = new ArrayList<>();
        for (Long productId : productIds)
            products.add(getById(productId));
        return products;
    }

    @Override
    @Transactional
    public Product create(Product product) {
        product.setId(null);
        product.setProductCategory(categoryService.getById(product.getProductCategory().getId()));
        return productRepo.save(product);
    }

    @Override
    @Transactional
    public Product update(Product product) {
        if (productRepo.findById(product.getId()).isEmpty())
            throw new ProductNotFoundException(ErrorMessage.PRODUCT_ID_NOT_FOUND);
        product.setProductCategory(categoryService.getById(product.getProductCategory().getId()));
        return productRepo.save(product);
    }

    @Override
    @Transactional
    public String updateProductsPrices(List<Long> productIDs, Double discount) {
        List<Product> products = getProductsByIds(productIDs);
        products.forEach(
                p -> {
                    p.setCurrentPrice(
                            p.getOriginalPrice()
                                    .divide(BigDecimal.valueOf(100))
                                    .multiply(BigDecimal.valueOf(100 - discount)));
                    productRepo.save(p);
                });
        return "%.2f DISCOUNT APPLIED".formatted(discount);
    }

    @Override
    @Transactional
    public String delete(Long productId) {
        if (productRepo.findById(productId).isEmpty())
            throw new ProductNotFoundException(ErrorMessage.PRODUCT_ID_NOT_FOUND);

        productRepo.deleteById(productId);
        return "PRODUCT DELETED";
    }
}
