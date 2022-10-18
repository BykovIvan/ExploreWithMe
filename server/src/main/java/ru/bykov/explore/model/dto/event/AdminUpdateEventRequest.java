package ru.bykov.explore.model.dto.event;

import lombok.*;
import ru.bykov.explore.model.dto.LocationDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateEventRequest {
    private String annotation;
    private Long category;
    private String description;
    private String eventDate;
    private LocationDto location;
    private Boolean paid;
    private Long participantLimit;
    private Boolean requestModeration;
    private String title;
    private Boolean commentModeration;
}
