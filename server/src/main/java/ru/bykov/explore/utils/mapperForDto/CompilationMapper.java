package ru.bykov.explore.utils.mapperForDto;

import ru.bykov.explore.model.Compilation;
import ru.bykov.explore.model.dto.copmilation.NewCompilationDto;

public class CompilationMapper {
    public static NewCompilationDto toCompilationDto(Compilation compilation) {
        return NewCompilationDto.builder()
//                .id(compilation.getId())
//                .events(compilation.getEvents())
                .build();
    }


    //TODO сделать по тз
    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
//                .events(newCompilationDto.getEvents())
                .build();
    }
}
