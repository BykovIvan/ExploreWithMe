package ru.bykov.explore.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.bykov.explore.model.dto.NewCategoryDto;
import ru.bykov.explore.services.CategoryService;

import javax.validation.Valid;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "admin/categories")
public class CategoryControllerAdmin {

    private final CategoryService categoryService;

    @PostMapping()
    public NewCategoryDto create (@Valid @RequestBody NewCategoryDto newCategoryDto){
        log.info("Получен запрос к эндпоинту /admin/categories метод POST");
        return categoryService.createFromAdmin(newCategoryDto);
    }

    @PatchMapping
    public NewCategoryDto update(@RequestBody NewCategoryDto newCategoryDto){
        log.info("Получен запрос к эндпоинту /admin/categories метод POST");
        return categoryService.updateFromAdmin(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    public void deleteById(@PathVariable("catId") Long catId) {
        log.info("Получен запрос к эндпоинту /admin/categories удаление по id {}. Метод DELETE", catId);
        categoryService.deleteFromAdminByCatId(catId);
    }




}
