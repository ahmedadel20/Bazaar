package com.bazaar.inventory.service;

import com.bazaar.inventory.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAll();
    Category getById(Long id);
    Category create(Category category);
    Category update(Category category);
    String delete(Long id);
}
