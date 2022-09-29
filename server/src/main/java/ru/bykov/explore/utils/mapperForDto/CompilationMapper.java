package ru.bykov.explore.utils.mapperForDto;

import ru.bykov.explore.model.Compilation;
import ru.bykov.explore.model.dto.CompilationDto;

public class CompilationMapper {
    public static CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(compilation.getEvents())
                .build();
    }


    //TODO сделать по тз
    public static Compilation toCompilation(CompilationDto compilationDto) {
        return Compilation.builder()
                .events(compilationDto.getEvents())
                .build();
    }
}
