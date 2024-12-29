package com.bazaar.inventory.dto;

import com.bazaar.inventory.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public Category toCategory(CategoryDTO categoryDto) {
        return Category
                .builder()
                .id(categoryDto.id())
                .name(categoryDto.name())
                .build();
    }

    public CategoryDTO toCategoryDTO(Category category) {
        return CategoryDTO
                .builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
