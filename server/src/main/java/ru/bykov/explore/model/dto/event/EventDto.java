package ru.bykov.explore.model.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bykov.explore.model.Category;
import ru.bykov.explore.model.Location;
import ru.bykov.explore.model.dto.user.UserShortDto;
import ru.bykov.explore.utils.StateOfEvent;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private Long id;
    private String annotation;
    private Category category;
    private Long confirmedRequests;
    private LocalDateTime createdOn;
    private String description;
    private UserShortDto initiator;
    private Location location;
    private Boolean paid;
    private Long participantLimit;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private StateOfEvent state;
    private String title;
    private Long views;
}
