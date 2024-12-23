package com.bazaar.inventory.service;

import com.bazaar.inventory.dto.CategoryDTO;
import com.bazaar.inventory.dto.CategoryMapper;
import com.bazaar.inventory.entity.Category;
import com.bazaar.inventory.repo.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private CategoryRepository categoryRepo;
    private CategoryMapper categoryMapper;

    @Autowired
    public CategoryService (CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepo = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public List<Category> getAll() {
        return categoryRepo.findAll();
    }

    public Category getById(Long id) {
        var category = categoryRepo.findById(id);
        if (category.isEmpty())
            throw new RuntimeException("No Category Found with ID %d".formatted(id));
        return category.get();
    }

    @Transactional
    public Category createCategory(CategoryDTO categoryDto) {
        var category = categoryMapper.toCategory(categoryDto);
        category.setId(null);
        return categoryRepo.save(category);
    }

    @Transactional
    public Category updateCategory(CategoryDTO categoryDto) {
        if (categoryRepo.findById(categoryDto.id()).isEmpty())
            throw new RuntimeException("No Category Found with ID %d".formatted(categoryDto.id()));
        return categoryRepo.save(categoryMapper.toCategory(categoryDto));
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (categoryRepo.findById(id).isEmpty())
            throw new RuntimeException("No Category Found with ID %d".formatted(id));
        categoryRepo.deleteById(id);
    }
}
