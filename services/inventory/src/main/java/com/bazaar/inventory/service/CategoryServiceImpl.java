package com.bazaar.inventory.service;

import com.bazaar.inventory.constant.ErrorMessage;
import com.bazaar.inventory.entity.Category;
import com.bazaar.inventory.exception.CategoryDuplicateNameException;
import com.bazaar.inventory.exception.CategoryInUseException;
import com.bazaar.inventory.exception.CategoryNotFoundException;
import com.bazaar.inventory.repo.CategoryRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
// @AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository categoryRepo;
    private ProductService productService;

    public CategoryServiceImpl(@Lazy ProductService productService, CategoryRepository categoryRepo) {
        this.productService = productService;
        this.categoryRepo = categoryRepo;
    }

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
    @Transactional
    public Category create(Category category) {
        category.setId(null);
        Optional<Category> existingCategory = categoryRepo.findByName(category.getName());
        if (existingCategory.isPresent()) {
            throw new CategoryDuplicateNameException(ErrorMessage.DUPLICATE_CATEOGRY_NAME);
        }
        return categoryRepo.save(category);
    }

    @Override
    @Transactional
    public Category update(Category category) {
        if (categoryRepo.findById(category.getId()).isEmpty())
            throw new CategoryNotFoundException(ErrorMessage.CATEGORY_ID_NOT_FOUND);
        if (categoryRepo.findByName(category.getName()).isPresent())
            throw new CategoryDuplicateNameException(ErrorMessage.DUPLICATE_CATEOGRY_NAME);
        return categoryRepo.save(category);
    }

    @Override
    @Transactional
    public String delete(Long id) {
        Optional<Category> optionalCateogry = categoryRepo.findById(id);
        if (optionalCateogry.isEmpty())
            throw new CategoryNotFoundException(ErrorMessage.CATEGORY_ID_NOT_FOUND);
        if (!productService.getProductsByCategory(optionalCateogry.get()).isEmpty())
            throw new CategoryInUseException(ErrorMessage.CATEGORY_IN_USE);
        categoryRepo.deleteById(id);
        return "CATEGORY DELETED";
    }
}
