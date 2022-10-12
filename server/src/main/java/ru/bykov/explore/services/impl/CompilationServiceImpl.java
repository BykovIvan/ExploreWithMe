package ru.bykov.explore.services.impl;

import lombok.RequiredArgsConstructor;
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
import ru.bykov.explore.utils.mapperForDto.CompilationMapper;
import ru.bykov.explore.utils.mapperForDto.EventMapper;

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
    public List<CompilationDto> findAllForAll() {
        return compilationRepository.findAll().stream()
                .map((Compilation compilation) -> CompilationMapper.toCompilationDto(compilation, compilation.getEvents().stream()
                        .map((Event event) -> EventMapper.toEventShortDto(event, 999L))
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto findByIdForAll(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new EntityNotFoundException(Compilation.class, "id", compId.toString()));
        return CompilationMapper.toCompilationDto(compilation, compilation.getEvents().stream()
                .map((Event event) -> EventMapper.toEventShortDto(event, 999L))
                .collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public CompilationDto createFromAdmin(NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getEvents() == null || newCompilationDto.getEvents().isEmpty()) {
            Compilation compilation = CompilationMapper.toCompilation(newCompilationDto, null);
            Compilation compilationNew = compilationRepository.save(compilation);
            compilationRepository.flush();
            return CompilationMapper.toCompilationDto(compilationNew, null);
        }
        List<Event> listOfEvent = eventRepository.findAllById(newCompilationDto.getEvents());
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto, listOfEvent);
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation), compilation.getEvents().stream()
                .map((Event event) -> EventMapper.toEventShortDto(event, 999L))
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
//        Optional<Event> eventGet = eventRepository.findById(eventId);
//        Event eventNew;
//        if (eventGet.isPresent()){
//            eventNew = eventGet.get();
//        }
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
