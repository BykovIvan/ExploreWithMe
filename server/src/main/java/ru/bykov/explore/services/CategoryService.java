package ru.bykov.explore.services;

import ru.bykov.explore.model.dto.category.CategoryDto;
import ru.bykov.explore.model.dto.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getAllForAllUsers();

    CategoryDto getByIdForAllUsers(Long id);

    CategoryDto createFromAdmin(NewCategoryDto newCategoryDto);

    CategoryDto updateFromAdmin(CategoryDto CategoryDto);

    void deleteFromAdminByCatId(Long catId);

}
