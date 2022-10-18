package ru.bykov.explore.model.dto.event;

import lombok.*;
import ru.bykov.explore.model.Category;
import ru.bykov.explore.model.dto.LocationDto;
import ru.bykov.explore.model.dto.user.UserShortDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {
    private String annotation;
    private Category category;
    private Long confirmedRequests;
    private String createdOn;
    private String description;
    private String eventDate;
    private Long id;
    private UserShortDto initiator;
    private LocationDto location;
    private Boolean paid;
    private Long participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private String state;
    private String title;
    private Long views;
    private Boolean commentModeration;
}
