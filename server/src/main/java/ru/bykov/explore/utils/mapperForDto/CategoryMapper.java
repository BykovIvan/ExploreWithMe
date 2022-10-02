package ru.bykov.explore.utils.mapperForDto;

import ru.bykov.explore.model.Category;
import ru.bykov.explore.model.dto.category.CategoryDto;
import ru.bykov.explore.model.dto.category.NewCategoryDto;

public class CategoryMapper {
    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category toCategory(NewCategoryDto newCategoryDto) {
        return Category.builder()
                .name(newCategoryDto.getName())
                .build();
    }
}
