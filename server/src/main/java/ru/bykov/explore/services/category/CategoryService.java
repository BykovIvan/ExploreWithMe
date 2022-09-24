package ru.bykov.explore.services.category;

import ru.bykov.explore.model.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getAll();

    CategoryDto getById(Long id);
}
