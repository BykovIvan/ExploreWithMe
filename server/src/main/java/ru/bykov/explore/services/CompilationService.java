package ru.bykov.explore.services;

import ru.bykov.explore.model.dto.copmilation.NewCompilationDto;

import java.util.List;

public interface CompilationService {

    List<NewCompilationDto> getAllForAll();

    NewCompilationDto getByIdForAll(Long compilationId);

    NewCompilationDto createFromAdmin(NewCompilationDto newCompilationDto);

    void deleteByIdFromAdmin(Long compId);

    void deleteEventFromCompFromAdmin(Long compId, Long eventId);

    void addEventToCompFromAdmin(Long compId, Long eventId);

    void deleteCompFromMainPageFromAdmin(Long compId);

    void addCompFromMainPageFromAdmin(Long compId);
}
