package ru.bykov.explore.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.bykov.explore.model.dto.category.CategoryDto;
import ru.bykov.explore.model.dto.category.NewCategoryDto;
import ru.bykov.explore.services.CategoryService;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "admin/categories")
public class CategoryControllerAdmin {

    private final CategoryService categoryService;

    @PatchMapping
    public CategoryDto update(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Получен запрос к эндпоинту /admin/categories изменение категории администратором. Метод PATCH");
        return categoryService.updateFromAdmin(categoryDto);
    }

    @PostMapping()
    public CategoryDto create(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Получен запрос к эндпоинту /admin/categories создание категории администратором. Метод POST");
        return categoryService.createFromAdmin(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    public void deleteById(@PathVariable("catId") Long catId) {
        log.info("Получен запрос к эндпоинту /admin/categories удаление категории по id {} администратором. Метод DELETE", catId);
        categoryService.deleteFromAdminByCatId(catId);
    }


}
