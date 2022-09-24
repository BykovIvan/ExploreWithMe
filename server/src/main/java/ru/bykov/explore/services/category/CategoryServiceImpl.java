package ru.bykov.explore.services.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bykov.explore.exceptions.NotFoundException;
import ru.bykov.explore.model.dto.CategoryDto;
import ru.bykov.explore.repositories.CategoryRepository;
import ru.bykov.explore.utils.mappingForDto.CategoryMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getAll() {
        return categoryRepository.findAll().stream()
                .map(CategoryMapping::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(Long id) {
        return CategoryMapping.toCategoryDto(categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Нет такой категории!")));
    }


}