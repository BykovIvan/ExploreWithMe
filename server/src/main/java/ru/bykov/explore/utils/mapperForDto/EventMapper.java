package ru.bykov.explore.utils.mapperForDto;

import ru.bykov.explore.model.Event;
import ru.bykov.explore.model.dto.event.EventDto;
import ru.bykov.explore.model.dto.event.NewEventDto;
import ru.bykov.explore.model.dto.event.EventShortDto;


//TODO весь класс необходимо сделать
public class EventMapper {
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

    //TODO
    public static Event toEvent(NewEventDto newEventDto) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .build();
    }
}
