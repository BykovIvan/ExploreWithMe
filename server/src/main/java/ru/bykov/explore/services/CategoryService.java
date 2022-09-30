package ru.bykov.explore.services;

import ru.bykov.explore.model.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getAllForAllUsers();

    CategoryDto getByIdForAllUsers(Long id);

    CategoryDto createFromAdmin(CategoryDto categoryDto);

    CategoryDto updateFromAdmin(CategoryDto categoryDto);

    void deleteFromAdminByCatId(Long catId);

}
