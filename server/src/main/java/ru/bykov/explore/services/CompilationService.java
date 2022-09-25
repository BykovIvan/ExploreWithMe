package ru.bykov.explore.services;

import ru.bykov.explore.model.dto.CompilationDto;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> getAllForAll();

    CompilationDto getByIdForAll(Long compilationId);

}
