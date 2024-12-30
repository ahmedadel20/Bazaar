package com.bazaar.inventory.service;

import com.bazaar.inventory.constant.ErrorMessage;
import com.bazaar.inventory.entity.Category;
import com.bazaar.inventory.exception.CategoryDuplicateNameException;
import com.bazaar.inventory.exception.CategoryInUseException;
import com.bazaar.inventory.exception.CategoryNotFoundException;
import com.bazaar.inventory.repo.CategoryRepository;
import com.bazaar.inventory.repo.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService{
    private CategoryRepository categoryRepo;
    private ProductRepository productRepo;

    @Override
    public List<Category> getAll() {
        return categoryRepo.findAll();
    }

    @Override
    public Category getById(Long id) {
        var category = categoryRepo.findById(id);
        if (category.isEmpty())
            throw new CategoryNotFoundException(ErrorMessage.CATEGORY_ID_NOT_FOUND);
        return category.get();
    }

    @Override
    public Category getByName(String name) {
        var category = categoryRepo.findByName(name);
        if (category.isEmpty())
            throw new CategoryNotFoundException(ErrorMessage.CATEGORY_NAME_NOT_FOUND);
        return category.get();
    }

    @Override
    public Category create(Category category) {
        category.setId(null);
        Optional<Category> categoryOptional = categoryRepo.findByName(category.getName());
        if (categoryOptional.isPresent()) {
            throw new CategoryDuplicateNameException(ErrorMessage.DUPLICATE_CATEOGRY_NAME);
        }
        return categoryRepo.save(category);
    }

    @Override
    public Category update(Category category) {
        if (categoryRepo.findById(category.getId()).isEmpty())
            throw new CategoryNotFoundException(ErrorMessage.CATEGORY_ID_NOT_FOUND);
        return categoryRepo.save(category);
    }

    @Override
    public String delete(Long id) {
        Optional<Category> optionalCateogry = categoryRepo.findById(id);
        if (optionalCateogry.isEmpty())
            throw new CategoryNotFoundException(ErrorMessage.CATEGORY_ID_NOT_FOUND);
        if (!productRepo.findByProductCategory(optionalCateogry.get()).isEmpty())
            throw new CategoryInUseException(ErrorMessage.CATEGORY_IN_USE);
        categoryRepo.deleteById(id);
        return "CATEGORY DELETED";
    }

}
