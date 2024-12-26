package com.bazaar.inventory.controller;
import com.bazaar.inventory.dto.CategoryDTO;
import com.bazaar.inventory.dto.CategoryMapper;
import com.bazaar.inventory.service.CategoryServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDTO> getCategories() {
        return categoryService
                .getAll()
                .stream()
                .map(categoryMapper::toCategoryDTO)
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
    public CategoryDTO createCategory(@RequestBody @Valid CategoryDTO categoryDto) {
        return categoryMapper.toCategoryDTO(
                categoryService.create(
                        categoryMapper.toCategory(categoryDto)
                )
        );
    }

    @PutMapping()
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public CategoryDTO updateCategory(@RequestBody CategoryDTO categoryDto) {
        return categoryMapper.toCategoryDTO(
                categoryService.update(
                        categoryMapper.toCategory(categoryDto)
                )
        );
    }

    @DeleteMapping("/{categoryId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String deleteCategory(@PathVariable Long categoryId) {
        return categoryService.delete(categoryId);
    }
}
