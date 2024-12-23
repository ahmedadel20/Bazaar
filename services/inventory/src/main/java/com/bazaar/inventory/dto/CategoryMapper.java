package com.bazaar.inventory.dto;

import com.bazaar.inventory.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public Category toCategory(CategoryDTO categoryDto) {
        return new Category(categoryDto.id(), categoryDto.name());
    }

    public CategoryDTO toCategoryDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getName());
    }
}
