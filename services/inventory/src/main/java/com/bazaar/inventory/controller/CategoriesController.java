package com.bazaar.inventory.controller;
import com.bazaar.inventory.dto.CategoryDTO;
import com.bazaar.inventory.dto.CategoryMapper;
import com.bazaar.inventory.service.CategoryServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/categories")
public class CategoriesController {
    private CategoryServiceImp categoryService;
    private CategoryMapper categoryMapper;

    @Autowired
    public CategoriesController(CategoryServiceImp categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping()
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDTO> getCategories() {
        return categoryService
                .getAll()
                .stream()
                .map(category -> categoryMapper.toCategoryDTO(category))
                .toList();
    }

    @GetMapping("/{categoryId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public CategoryDTO getCategoryById(@PathVariable @Valid Long categoryId) {
        return categoryMapper.toCategoryDTO(categoryService.getById(categoryId));
    }

    @PostMapping()
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public CategoryDTO createProduct(@RequestBody @Valid CategoryDTO categoryDto) {
        return categoryMapper.toCategoryDTO(
                categoryService.create(
                        categoryMapper.toCategory(categoryDto)
                )
        );
    }

    @PutMapping()
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public CategoryDTO updateProduct(@RequestBody CategoryDTO categoryDto) {
        return categoryMapper.toCategoryDTO(
                categoryService.update(
                        categoryMapper.toCategory(categoryDto)
                )
        );
    }

    @DeleteMapping("/{categoryId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String deleteProduct(@PathVariable Long categoryId) {
        categoryService.delete(categoryId);
        return "PRODUCT DELETED";
    }
}
