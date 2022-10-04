package ru.bykov.explore.utils.mapperForDto;

import ru.bykov.explore.model.Event;
import ru.bykov.explore.model.dto.event.EventFullDto;
import ru.bykov.explore.model.dto.event.EventShortDto;
import ru.bykov.explore.model.dto.event.NewEventDto;

import java.time.format.DateTimeFormatter;


//TODO весь класс необходимо сделать
public class EventMapper {
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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

    public static EventFullDto toEventFullDto(Event event, Long views) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(event.getCategory())
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn().format(formatter))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(formatter))
                .id(event.getId())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(LocationMapper.toLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn().format(formatter))
                .requestModeration(event.getRequestModeration())
                .state(event.getState().toString())
                .title(event.getTitle())
                .views(views)
                .build();
    }
}
