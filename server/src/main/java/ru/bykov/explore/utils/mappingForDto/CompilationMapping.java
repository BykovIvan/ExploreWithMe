package ru.bykov.explore.utils.mappingForDto;

import ru.bykov.explore.model.Compilation;
import ru.bykov.explore.model.dto.CompilationDto;

public class CompilationMapping {
    public static CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(compilation.getEvents())
                .build();
    }

    public static Compilation toCompilation(CompilationDto compilationDto) {
        return Compilation.builder()
                .events(compilationDto.getEvents())
                .build();
    }
}
