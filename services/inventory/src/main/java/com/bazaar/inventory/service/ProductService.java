package com.bazaar.inventory.service;

import com.bazaar.inventory.entity.Category;
import com.bazaar.inventory.entity.Product;

import java.util.*;

public interface ProductService {
    List<Product> getAll();
    Product getById(Long id);
    List<Product> getProductsByCategory(Category category);
    List<Product> getProductsByCategories(List<Long> categoryIds);
    List<Product> getProductsByIds(List<Long> productIds);
    Product create(Product product);
    Product update(Product product);
    String updateProductsPrices(List<Long> productIDs, float discount);
    String delete(Long id);
}
