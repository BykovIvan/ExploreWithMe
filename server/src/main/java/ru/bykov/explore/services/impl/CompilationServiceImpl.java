package ru.bykov.explore.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bykov.explore.exceptions.NotFoundException;
import ru.bykov.explore.model.dto.CompilationDto;
import ru.bykov.explore.repositories.CompilationRepository;
import ru.bykov.explore.services.CompilationService;
import ru.bykov.explore.utils.mapperForDto.CompilationMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    @Override
    public List<CompilationDto> getAllForAll() {
        return compilationRepository.findAll().stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getByIdForAll(Long compilationId) {
        return CompilationMapper.toCompilationDto(compilationRepository.findById(compilationId).orElseThrow(() -> new NotFoundException("Нет такого события!")));
    }
}
