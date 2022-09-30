package ru.bykov.explore.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bykov.explore.exceptions.NoParamInRequestException;
import ru.bykov.explore.exceptions.NotFoundException;
import ru.bykov.explore.model.Category;
import ru.bykov.explore.model.dto.CategoryDto;
import ru.bykov.explore.repositories.CategoryRepository;
import ru.bykov.explore.services.CategoryService;
import ru.bykov.explore.utils.mapperForDto.CategoryMapper;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getAllForAllUsers() {
        return categoryRepository.findAll().stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getByIdForAllUsers(Long id) {
        return CategoryMapper.toCategoryDto(categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Нет такой категории!")));
    }

    @Override
    public CategoryDto createFromAdmin(CategoryDto categoryDto) {
        @Valid Category category = CategoryMapper.toCategory(categoryDto);
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto updateFromAdmin(CategoryDto categoryDto) {
        if (categoryDto.getId() == null || categoryDto.getName() == null) {
            throw new NoParamInRequestException("Введены неверные параметры!");
        }
        Category category = categoryRepository.findById(categoryDto.getId()).orElseThrow(() -> new NotFoundException("Нет такой категории!"));
        category.setName(categoryDto.getName());
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