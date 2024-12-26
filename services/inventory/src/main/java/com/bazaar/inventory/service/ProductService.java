package com.bazaar.inventory.service;

import com.bazaar.inventory.entity.Product;

import java.util.*;

public interface ProductService {
    List<Product> getAll();
    Product getById(Long id);
    Product create(Product product);
    Product update(Product product);
    String delete(Long id);
}
