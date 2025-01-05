package com.bazaar.inventory.controller;
import com.bazaar.inventory.dto.CategoryDto;
import com.bazaar.inventory.dto.CategoryMapper;
import com.bazaar.inventory.service.CategoryServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Categories", description = "Controller for handling mappings for product categories")
@Controller
@RequestMapping("/api/v1/categories")
@AllArgsConstructor
public class CategoriesController {
    private CategoryServiceImpl categoryService;
    private CategoryMapper categoryMapper;

    @GetMapping()
    @ResponseBody
    public List<CategoryDto> getCategories() {
        return categoryService
                .getAll()
                .stream()
                .map(categoryMapper::toCategoryDTO)
                .toList();
    }

    @GetMapping("/{categoryId}")
    @ResponseBody
    public CategoryDto getCategoryById(@PathVariable Long categoryId) {
        return categoryMapper.toCategoryDTO(categoryService.getById(categoryId));
    }

    @PostMapping()
    @ResponseBody
    public CategoryDto createCategory(@RequestBody @Valid CategoryDto categoryDto) {
        return categoryMapper.toCategoryDTO(
                categoryService.create(
                        categoryMapper.toCategory(categoryDto)
                )
        );
    }

    @PutMapping()
    @ResponseBody
    public CategoryDto updateCategory(@RequestBody CategoryDto categoryDto) {
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
