package ru.bykov.explore.utils.mapperForDto;

import ru.bykov.explore.model.Compilation;
import ru.bykov.explore.model.Event;
import ru.bykov.explore.model.dto.copmilation.CompilationDto;
import ru.bykov.explore.model.dto.copmilation.NewCompilationDto;
import ru.bykov.explore.model.dto.event.EventShortDto;

import java.util.ArrayList;
import java.util.List;

public class CompilationMapper {
    public static CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> listOdEvent) {
        CompilationDto compilationDto = CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
        compilationDto.setEvents(listOdEvent != null ? listOdEvent : new ArrayList<>());
        return compilationDto;
    }

    public static Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> listOdEvent) {
        Compilation compilation = Compilation.builder()
                .title(newCompilationDto.getTitle())
                .build();
        compilation.setPinned(newCompilationDto.getPinned() != null ? newCompilationDto.getPinned() : false);
        compilation.setEvents(listOdEvent != null ? listOdEvent : new ArrayList<>());
        return compilation;
    }
}
