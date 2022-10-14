package ru.bykov.explore.utils.mapperForDto;

import ru.bykov.explore.model.Compilation;
import ru.bykov.explore.model.Event;
import ru.bykov.explore.model.dto.copmilation.CompilationDto;
import ru.bykov.explore.model.dto.copmilation.NewCompilationDto;
import ru.bykov.explore.model.dto.event.EventShortDto;

import java.util.ArrayList;
import java.util.List;

public class CompilationMapper {
    public static CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> eventShortDto) {
        CompilationDto compilationDto = CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
        if (eventShortDto != null) {
            compilationDto.setEvents(eventShortDto);
        } else {
            compilationDto.setEvents(new ArrayList<>());
        }
        return compilationDto;
    }

    public static Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> listOdEvent) {
        Compilation compilation = Compilation.builder()
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .build();
        if (newCompilationDto.getPinned() != null){
            compilation.setPinned(newCompilationDto.getPinned());
        } else {
            compilation.setPinned(false);
        }
        if (listOdEvent != null) {
            compilation.setEvents(listOdEvent);
        } else {
            compilation.setEvents(new ArrayList<>());
        }
        return compilation;
    }
}
