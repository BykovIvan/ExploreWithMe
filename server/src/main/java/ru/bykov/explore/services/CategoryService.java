package ru.bykov.explore.services;

import ru.bykov.explore.model.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getAllForAllUsers();

    CategoryDto getByIdForAllUsers(Long id);

    CategoryDto createByAdmin(CategoryDto categoryDto);

    CategoryDto updateByAdmin(CategoryDto categoryDto);

    void deleteByAdminByCatId(Long catId);
}
