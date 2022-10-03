package ru.bykov.explore.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bykov.explore.exceptions.NotFoundException;
import ru.bykov.explore.model.Compilation;
import ru.bykov.explore.model.Event;
import ru.bykov.explore.model.dto.copmilation.CompilationDto;
import ru.bykov.explore.model.dto.copmilation.NewCompilationDto;
import ru.bykov.explore.repositories.CompilationRepository;
import ru.bykov.explore.repositories.EventRepository;
import ru.bykov.explore.services.CompilationService;
import ru.bykov.explore.utils.mapperForDto.CompilationMapper;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CompilationDto> getAllForAll() {
        return compilationRepository.findAll().stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getByIdForAll(Long compilationId) {
        return CompilationMapper.toCompilationDto(compilationRepository.findById(compilationId).orElseThrow(() -> new NotFoundException("Нет такой подборки!")));
    }

    @Override
    public CompilationDto createFromAdmin(NewCompilationDto newCompilationDto) {
        List<Event> listOfEvent = eventRepository.findAllById(newCompilationDto.getEvents());
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto, listOfEvent);
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteByIdFromAdmin(Long compId) {
        compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Нет такой подборки!"));
        compilationRepository.deleteById(compId);
    }

    @Override
    public void deleteEventFromCompFromAdmin(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Нет такой подборки!"));
        compilation.getEvents().removeIf(nextEvent -> nextEvent.getId() == (eventId));
    }

    @Override
    public void addEventToCompFromAdmin(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Нет такой подборки!"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Нет такого события!"));
        compilation.getEvents().add(event);
        compilationRepository.save(compilation);
    }

    @Override
    public void deleteCompFromMainPageFromAdmin(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Нет такой подборки!"));
        //TODO сделать и уточнить про главную страницу
    }

    @Override
    public void addCompFromMainPageFromAdmin(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Нет такой подборки!"));
        //TODO сделать и уточнить про главную страницу
    }
}
