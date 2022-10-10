package ru.bykov.explore.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bykov.explore.exceptions.EntityNotFoundException;
import ru.bykov.explore.model.Compilation;
import ru.bykov.explore.model.Event;
import ru.bykov.explore.model.dto.copmilation.CompilationDto;
import ru.bykov.explore.model.dto.copmilation.NewCompilationDto;
import ru.bykov.explore.repositories.CompilationRepository;
import ru.bykov.explore.repositories.EventRepository;
import ru.bykov.explore.services.CompilationService;
import ru.bykov.explore.utils.mapperForDto.CompilationMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CompilationDto> getAllForAll() {
        //TODO сделать как то про views подумать
        Long views = 0L;
        return compilationRepository.findAll().stream()
                .map((Compilation compilation) -> CompilationMapper.toCompilationDto(compilation, views))
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getByIdForAll(Long compId) {
        //TODO сделать как то про views подумать
        Long views = 0L;
        return CompilationMapper.toCompilationDto(compilationRepository.findById(compId).orElseThrow(() -> new EntityNotFoundException(Compilation.class, "id", compId.toString())), views);
    }

    @Transactional
    @Override
    public CompilationDto createFromAdmin(NewCompilationDto newCompilationDto) {
        List<Event> listOfEvent = eventRepository.findAllById(newCompilationDto.getEvents());
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto, listOfEvent);
        //TODO сделать как то про views подумать
        Long views = 0L;
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation), views);
    }

    @Transactional
    @Override
    public void deleteByIdFromAdmin(Long compId) {
        compilationRepository.findById(compId).orElseThrow(() -> new EntityNotFoundException(Compilation.class, "id", compId.toString()));
        compilationRepository.deleteById(compId);
    }

    @Transactional
    @Override
    //TODO проверить
    public void deleteEventFromCompFromAdmin(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new EntityNotFoundException(Compilation.class, "id", compId.toString()));
        compilation.getEvents().removeIf(nextEvent -> nextEvent.getId() == (eventId));
    }

    @Override
    public void addEventToCompFromAdmin(Long compId, Long eventId) {
//        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new EntityNotFoundException(Compilation.class, "id", compId.toString()));
        Optional<Compilation> compilationOpt = compilationRepository.findById(compId);
        if (compilationOpt.isEmpty()){
            throw new EntityNotFoundException(Compilation.class, "id", compId.toString());
        }
        Compilation compilation = compilationOpt.get();
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString()));
        compilation.getEvents().add(event);
        compilationRepository.save(compilation);
    }

    @Transactional
    @Override
    public void deleteCompFromMainPageFromAdmin(Long compId) {
        compilationRepository.findById(compId).orElseThrow(() -> new EntityNotFoundException(Compilation.class, "id", compId.toString()));
        compilationRepository.setPinnedByCompId(false, compId);
    }

    @Transactional
    @Override
    public void addCompFromMainPageFromAdmin(Long compId) {
        compilationRepository.findById(compId).orElseThrow(() -> new EntityNotFoundException(Compilation.class, "id", compId.toString()));
        compilationRepository.setPinnedByCompId(true, compId);
    }
}
