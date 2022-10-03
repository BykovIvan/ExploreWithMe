package ru.bykov.explore.utils.mapperForDto;

import ru.bykov.explore.model.Event;
import ru.bykov.explore.model.dto.event.EventFullDto;
import ru.bykov.explore.model.dto.event.NewEventDto;
import ru.bykov.explore.model.dto.event.EventShortDto;


//TODO весь класс необходимо сделать
public class EventMapper {
    public static EventFullDto toEventDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .build();
    }

    public static EventShortDto toEventShortDto(Event event, Long views) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .build();
    }

    public static Event toEvent(EventFullDto eventFullDto) {
        return Event.builder()
                .annotation(eventFullDto.getAnnotation())
                .build();
    }

    //TODO
    public static Event toEvent(NewEventDto newEventDto) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .build();
    }
}
