package ru.bykov.explore.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.bykov.explore.model.dto.CategoryDto;
import ru.bykov.explore.services.CategoryService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "admin/categories")
public class CategoryControllerAdmin {

    private final CategoryService categoryService;

    @PostMapping()
    public CategoryDto create (@RequestBody CategoryDto categoryDto){
        log.info("Получен запрос к эндпоинту /admin/categories метод POST");
        return categoryService.createFromAdmin(categoryDto);
    }

    @PatchMapping
    public CategoryDto update(@RequestBody CategoryDto categoryDto){
        log.info("Получен запрос к эндпоинту /admin/categories метод POST");
        return categoryService.updateFromAdmin(categoryDto);
    }

    @DeleteMapping("/{catId}")
    public void deleteById(@PathVariable("catId") Long catId) {
        log.info("Получен запрос к эндпоинту /admin/categories удаление по id {}. Метод DELETE", catId);
        categoryService.deleteByAdminFromCatId(catId);
    }




}
