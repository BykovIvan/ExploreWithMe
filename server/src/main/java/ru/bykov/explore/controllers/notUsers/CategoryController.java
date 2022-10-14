package ru.bykov.explore.controllers.notUsers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.bykov.explore.model.dto.category.CategoryDto;
import ru.bykov.explore.services.CategoryService;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> allCategories(@RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
                                           @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        log.info("Получен запрос к эндпоинту /categories получение всех. Метод GET");
        return categoryService.findAllForAllUsers(from, size);
    }

    @GetMapping("/{catId}")
    public Optional<CategoryDto> categoryById(@PathVariable("catId") Long catId) {
        log.info("Получен запрос к эндпоинту /categories получение по id. Метод GET");
        return categoryService.findByIdForAllUsers(catId);
    }

}
