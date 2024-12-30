package com.bazaar.inventory.controller;
import com.bazaar.inventory.dto.CategoryDTO;
import com.bazaar.inventory.dto.CategoryMapper;
import com.bazaar.inventory.service.CategoryServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/categories")
@AllArgsConstructor
public class CategoriesController {
    private CategoryServiceImpl categoryService;
    private CategoryMapper categoryMapper;

    @GetMapping()
    @ResponseBody
    public List<CategoryDTO> getCategories() {
        return categoryService
                .getAll()
                .stream()
                .map(categoryMapper::toCategoryDTO)
                .toList();
    }

    @GetMapping("/{categoryId}")
    @ResponseBody
    public CategoryDTO getCategoryById(@PathVariable @Valid Long categoryId) {
        return categoryMapper.toCategoryDTO(categoryService.getById(categoryId));
    }

    @PostMapping()
    @ResponseBody
    public CategoryDTO createCategory(@RequestBody @Valid CategoryDTO categoryDto) {
        return categoryMapper.toCategoryDTO(
                categoryService.create(
                        categoryMapper.toCategory(categoryDto)
                )
        );
    }

    @PutMapping()
    @ResponseBody
    public CategoryDTO updateCategory(@RequestBody CategoryDTO categoryDto) {
        return categoryMapper.toCategoryDTO(
                categoryService.update(
                        categoryMapper.toCategory(categoryDto)
                )
        );
    }

    @DeleteMapping("/{categoryId}")
    @ResponseBody
    public String deleteCategory(@PathVariable Long categoryId) {
        return categoryService.delete(categoryId);
    }
}
