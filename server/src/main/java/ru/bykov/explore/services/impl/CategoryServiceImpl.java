package ru.bykov.explore.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bykov.explore.exceptions.NoParamInRequestException;
import ru.bykov.explore.exceptions.NotFoundException;
import ru.bykov.explore.services.CategoryService;
import ru.bykov.explore.model.Category;
import ru.bykov.explore.model.dto.NewCategoryDto;
import ru.bykov.explore.repositories.CategoryRepository;
import ru.bykov.explore.utils.mapperForDto.CategoryMapper;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<NewCategoryDto> getAllForAllUsers() {
        return categoryRepository.findAll().stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public NewCategoryDto getByIdForAllUsers(Long id) {
        return CategoryMapper.toCategoryDto(categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Нет такой категории!")));
    }

    @Override
    public NewCategoryDto createFromAdmin(NewCategoryDto newCategoryDto) {
        @Valid Category category = CategoryMapper.toCategory(newCategoryDto);
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public NewCategoryDto updateFromAdmin(NewCategoryDto newCategoryDto) {
        if (newCategoryDto.getId() == null || newCategoryDto.getName() == null) {
            throw new NoParamInRequestException("Введены неверные параметры!");
        }
        Category category = categoryRepository.findById(newCategoryDto.getId()).orElseThrow(() -> new NotFoundException("Нет такой категории!"));
        category.setName(newCategoryDto.getName());
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public void deleteFromAdminByCatId(Long catId) {
        if (catId == null) {
            throw new NoParamInRequestException("Введены неверные параметры!");
        }
        categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Нет такой категории!"));
        categoryRepository.deleteById(catId);
    }
}
