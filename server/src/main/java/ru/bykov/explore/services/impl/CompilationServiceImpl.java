package ru.bykov.explore.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bykov.explore.clientstat.StatClient;
import ru.bykov.explore.exceptions.EntityNotFoundException;
import ru.bykov.explore.model.Compilation;
import ru.bykov.explore.model.Event;
import ru.bykov.explore.model.dto.copmilation.CompilationDto;
import ru.bykov.explore.model.dto.copmilation.NewCompilationDto;
import ru.bykov.explore.repositories.CompilationRepository;
import ru.bykov.explore.repositories.EventRepository;
import ru.bykov.explore.services.CompilationService;
import ru.bykov.explore.utils.FromSizeSortPageable;
import ru.bykov.explore.utils.mapperForDto.CompilationMapper;
import ru.bykov.explore.utils.mapperForDto.EventMapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final StatClient statClient;

    @Override
    public List<CompilationDto> findAllForAllByParam(Boolean pinned, Integer from, Integer size) {
        return compilationRepository.findAllByPinned(pinned, FromSizeSortPageable.of(from, size, Sort.by(Sort.Direction.ASC, "id"))).stream()
                .map((Compilation compilation) -> CompilationMapper.toCompilationDto(compilation, compilation.getEvents().stream()
                        .map((Event event) -> EventMapper.toEventShortDto(event, statClient.getViews(event)))
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CompilationDto findByIdForAll(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new EntityNotFoundException(Compilation.class, "id", compId.toString()));
        return CompilationMapper.toCompilationDto(compilation, compilation.getEvents() != null ? compilation.getEvents().stream()
                .map((Event event) -> EventMapper.toEventShortDto(event, statClient.getViews(event)))
                .collect(Collectors.toList()) : new ArrayList<>());
    }

    @Override
    @Transactional
    public CompilationDto createFromAdmin(NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getEvents() == null || newCompilationDto.getEvents().isEmpty()) {
            Compilation compilation = CompilationMapper.toCompilation(newCompilationDto, null);
            return CompilationMapper.toCompilationDto(compilationRepository.save(compilation), null);
        }
        List<Event> listOfEvent = eventRepository.findAllById(newCompilationDto.getEvents());
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto, listOfEvent);
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation), compilation.getEvents().stream()
                .map((Event event) -> EventMapper.toEventShortDto(event, statClient.getViews(event)))
                .collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public void deleteByIdFromAdmin(Long compId) {
        compilationRepository.findById(compId).orElseThrow(() -> new EntityNotFoundException(Compilation.class, "id", compId.toString()));
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public void deleteEventFromCompFromAdmin(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new EntityNotFoundException(Compilation.class, "id", compId.toString()));
        List<Event> listOfEvents = compilation.getEvents();
        Iterator<Event> eventIterator = listOfEvents.iterator();
        while (eventIterator.hasNext()) {
            Event event = eventIterator.next();
            if (event.getId().equals(eventId)) {
                eventIterator.remove();
            }
        }
        compilation.setEvents(listOfEvents);
        compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public void addEventToCompFromAdmin(Long compId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString()));
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new EntityNotFoundException(Compilation.class, "id", compId.toString()));
        compilation.getEvents().add(event);
        compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public void deleteCompFromMainPageFromAdmin(Long compId) {
        compilationRepository.findById(compId).orElseThrow(() -> new EntityNotFoundException(Compilation.class, "id", compId.toString()));
        compilationRepository.setPinnedByCompId(false, compId);
    }

    @Override
    @Transactional
    public void addCompFromMainPageFromAdmin(Long compId) {
        compilationRepository.findById(compId).orElseThrow(() -> new EntityNotFoundException(Compilation.class, "id", compId.toString()));
        compilationRepository.setPinnedByCompId(true, compId);
    }
}
