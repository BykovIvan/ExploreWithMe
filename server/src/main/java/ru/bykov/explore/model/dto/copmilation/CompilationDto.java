package ru.bykov.explore.model.dto.copmilation;

import lombok.*;
import ru.bykov.explore.model.dto.event.EventShortDto;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    private List<EventShortDto> events; //список входящий в подборку
    private Long id;                    //идентификатор
    private Boolean pinned;             //закреплена ли подборка на главной странице
    private String title;               //заголовок подборки
}
