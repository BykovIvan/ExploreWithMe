package ru.bykov.explore.services;

import ru.bykov.explore.model.dto.category.CategoryDto;
import ru.bykov.explore.model.dto.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    /**
     * Получение категорий всеми пользователями
     * Getting categories for all users
     */
    List<CategoryDto> getAllForAllUsers();

    /**
     * Получение информации о категории по ее идентификатором всеми пользователями
     * Getting information about a category by its ID by all users
     */
    CategoryDto getByIdForAllUsers(Long id);

    /**
     * Изменение категории администратором
     * Changing category by administrator
     */
    CategoryDto updateFromAdmin(CategoryDto CategoryDto);

    /**
     * Добавление новой категории администратором
     * Adding a new category by administrator
     */
    CategoryDto createFromAdmin(NewCategoryDto newCategoryDto);

    /**
     * Удаление категории администратором
     * Removing a category by administrator
     */
    void deleteFromAdminByCatId(Long catId);

}
