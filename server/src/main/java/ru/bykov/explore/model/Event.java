package ru.bykov.explore.model;

import lombok.*;
import ru.bykov.explore.utils.StateOfEvent;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Event {
    private Long id;
    private String annotation;
    private Category category;
    private Long confirmedRequests;
    private LocalDateTime createdOn;
    private String description;
    private User initiator;
    private Location location;
    private Boolean paid;
    private Long participantLimit;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private StateOfEvent state;
    private String title;


}
