package com.bazaar.inventory.dto;

import com.bazaar.inventory.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public Category toCategory(CategoryDto categoryDto) {
        return Category
                .builder()
                .id(categoryDto.id())
                .name(categoryDto.name())
                .build();
    }

    public CategoryDto toCategoryDTO(Category category) {
        return CategoryDto
                .builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
