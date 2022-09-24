package ru.bykov.explore.utils.mappingForDto;

import ru.bykov.explore.model.Event;
import ru.bykov.explore.model.dto.event.EventDto;
import ru.bykov.explore.model.dto.event.EventShortDto;

public class EventMapping {
    public static EventDto toEventDto(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .build();
    }

    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .build();
    }

    public static Event toEvent(EventDto eventDto) {
        return Event.builder()
                .annotation(eventDto.getAnnotation())
                .build();
    }
}
