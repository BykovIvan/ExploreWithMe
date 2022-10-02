package ru.bykov.explore.model.dto.copmilation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bykov.explore.model.Event;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    private Long id;
    private List<Event> events;
//    private List<Long> events;
    private Boolean pinned;
    private String title;
}
