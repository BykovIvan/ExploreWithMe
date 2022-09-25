package ru.bykov.explore.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bykov.explore.exceptions.NotFoundException;
import ru.bykov.explore.model.dto.CompilationDto;
import ru.bykov.explore.repositories.CompilationRepository;
import ru.bykov.explore.services.CompilationService;
import ru.bykov.explore.utils.mappingForDto.CompilationMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    @Override
    public List<CompilationDto> getAllForAll() {
        return compilationRepository.findAll().stream()
                .map(CompilationMapping::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getByIdForAll(Long compilationId) {
        return CompilationMapping.toCompilationDto(compilationRepository.findById(compilationId).orElseThrow(() -> new NotFoundException("Нет такого события!")));
    }
}
