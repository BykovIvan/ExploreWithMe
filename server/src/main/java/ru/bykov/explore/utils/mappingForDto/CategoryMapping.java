package ru.bykov.explore.utils.mappingForDto;

import ru.bykov.explore.model.Category;
import ru.bykov.explore.model.dto.CategoryDto;

public class CategoryMapping {
    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category toCategory(CategoryDto categoryDto) {
        return Category.builder()
                .name(categoryDto.getName())
                .build();
    }
}
