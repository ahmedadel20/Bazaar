package com.bazaar.inventory.service;

import com.bazaar.inventory.constant.ErrorMessage;
import com.bazaar.inventory.entity.Category;
import com.bazaar.inventory.exception.CategoryDuplicateIdException;
import com.bazaar.inventory.exception.CategoryDuplicateNameException;
import com.bazaar.inventory.exception.CategoryNotFoundException;
import com.bazaar.inventory.repo.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService{
    private CategoryRepository categoryRepo;

    public List<Category> getAll() {
        return categoryRepo.findAll();
    }

    public Category getById(Long id) {
        var category = categoryRepo.findById(id);
        if (category.isEmpty())
            throw new CategoryNotFoundException(ErrorMessage.CATEGORY_ID_NOT_FOUND);
        return category.get();
    }

    @Transactional
    public Category create(Category category) {
        category.setId(null);
        Category savedCategory = null;
        try {
            savedCategory = categoryRepo.save(category);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(e.getMessage());
            throw new CategoryDuplicateNameException(ErrorMessage.DUPLICATE_CATEOGRY_NAME);
        }
        return savedCategory;
    }

    @Transactional
    public Category update(Category category) {
        if (categoryRepo.findById(category.getId()).isEmpty())
            throw new CategoryNotFoundException(ErrorMessage.CATEGORY_ID_NOT_FOUND);
        return categoryRepo.save(category);
    }

    @Transactional
    public void delete(Long id) {
        if (categoryRepo.findById(id).isEmpty())
            throw new CategoryNotFoundException(ErrorMessage.CATEGORY_ID_NOT_FOUND);
        categoryRepo.deleteById(id);
    }

}
