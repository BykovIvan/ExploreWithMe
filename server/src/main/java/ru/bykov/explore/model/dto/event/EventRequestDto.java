package ru.bykov.explore.model.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bykov.explore.model.User;
import ru.bykov.explore.utils.StateOfEvent;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDto {
    private Long id;
    private LocalDateTime created;
    private Long event;
    private User requester;
//    private StateOfEvent state;
    private String state;
}
