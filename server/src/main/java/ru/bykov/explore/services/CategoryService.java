package ru.bykov.explore.services;

import ru.bykov.explore.model.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    List<NewCategoryDto> getAllForAllUsers();

    NewCategoryDto getByIdForAllUsers(Long id);

    NewCategoryDto createFromAdmin(NewCategoryDto newCategoryDto);

    NewCategoryDto updateFromAdmin(NewCategoryDto newCategoryDto);

    void deleteFromAdminByCatId(Long catId);

}
