package ru.bykov.explore.utils.mapperForDto;

import ru.bykov.explore.model.Compilation;
import ru.bykov.explore.model.Event;
import ru.bykov.explore.model.dto.copmilation.CompilationDto;
import ru.bykov.explore.model.dto.copmilation.NewCompilationDto;

import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static CompilationDto toCompilationDto(Compilation compilation, Long views) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(compilation.getEvents()
                        .stream()
                        .map((Event event) -> EventMapper.toEventShortDto(event, views))
                        .collect(Collectors.toList()))
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }

    public static Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> listOdEvent) {
        return Compilation.builder()
                .events(listOdEvent)
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }
}
