package ru.bykov.explore.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bykov.explore.exceptions.EntityNotFoundException;
import ru.bykov.explore.model.Category;
import ru.bykov.explore.model.dto.category.CategoryDto;
import ru.bykov.explore.model.dto.category.NewCategoryDto;
import ru.bykov.explore.repositories.CategoryRepository;
import ru.bykov.explore.services.CategoryService;
import ru.bykov.explore.utils.FromSizeSortPageable;
import ru.bykov.explore.utils.mapperForDto.CategoryMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> findAllForAllUsers(Integer from, Integer size) {
        return categoryRepository.findAll(FromSizeSortPageable.of(from, size, Sort.by(Sort.Direction.ASC, "id"))).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto findByIdForAllUsers(Long catId) {
        return CategoryMapper.toCategoryDto(categoryRepository.findById(catId).orElseThrow(() -> new EntityNotFoundException(Category.class, "id", catId.toString())));
    }

    @Override
    @Transactional
    public CategoryDto updateFromAdmin(CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryDto.getId()).orElseThrow(() -> new EntityNotFoundException(Category.class, "id", categoryDto.getId().toString()));
        category.setName(categoryDto.getName());
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryDto createFromAdmin(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.toCategory(newCategoryDto);
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteFromAdminByCatId(Long catId) {
        categoryRepository.findById(catId).orElseThrow(() -> new EntityNotFoundException(Category.class, "id", catId.toString()));
        categoryRepository.deleteById(catId);
    }
}
