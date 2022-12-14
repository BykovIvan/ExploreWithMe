package ru.bykov.explore.model.dto.copmilation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bykov.explore.model.dto.event.EventShortDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    private List<EventShortDto> events; //список входящий в подборку
    private Long id;                    //идентификатор
    private Boolean pinned;             //закреплена ли подборка на главной странице
    private String title;               //заголовок подборки
}
