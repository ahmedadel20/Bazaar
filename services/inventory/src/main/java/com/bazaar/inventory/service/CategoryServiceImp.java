package com.bazaar.inventory.service;

import com.bazaar.inventory.entity.Category;
import com.bazaar.inventory.exception.CategoryNotFoundException;
import com.bazaar.inventory.repo.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
@Service
public class CategoryServiceImp implements CategoryService{
    private CategoryRepository categoryRepo;

    @Autowired
    public CategoryServiceImp(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public List<Category> getAll() {
        return categoryRepo.findAll();
    }

    public Category getById(Long id) {
        var category = categoryRepo.findById(id);
        if (category.isEmpty())
            throw new CategoryNotFoundException("No Category Found with ID %d".formatted(id));
        return category.get();
    }

    @Transactional
    public Category create(Category category) {
        category.setId(null);
        return categoryRepo.save(category);
    }

    @Transactional
    public Category update(Category category) {
        if (categoryRepo.findById(category.getId()).isEmpty())
            throw new CategoryNotFoundException("No Category Found with ID %d".formatted(category.getId()));
        return categoryRepo.save(category);
    }

    @Transactional
    public void delete(Long id) {
        if (categoryRepo.findById(id).isEmpty())
            throw new CategoryNotFoundException("No Category Found with ID %d".formatted(id));
        categoryRepo.deleteById(id);
    }

}
