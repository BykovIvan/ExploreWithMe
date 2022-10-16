package ru.bykov.explore.utils.mapperForDto;

import ru.bykov.explore.model.Category;
import ru.bykov.explore.model.Event;
import ru.bykov.explore.model.Location;
import ru.bykov.explore.model.User;
import ru.bykov.explore.model.dto.comment.CommentDtoForEvent;
import ru.bykov.explore.model.dto.event.EventDtoWithComments;
import ru.bykov.explore.model.dto.event.EventFullDto;
import ru.bykov.explore.model.dto.event.EventShortDto;
import ru.bykov.explore.model.dto.event.NewEventDto;
import ru.bykov.explore.utils.EventState;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EventMapper {
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static EventShortDto toEventShortDto(Event event, Long views) {
        EventShortDto eventShortDto = EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate().format(formatter))
                .id(event.getId())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();
        eventShortDto.setViews(views != null ? views : 0);
        return eventShortDto;
    }

    public static Event toEvent(NewEventDto newEventDto, Category category, User user, Location location) {
        Event event = Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .confirmedRequests(0L)
                .createdOn(LocalDateTime.now())
                .description(newEventDto.getDescription())
                .eventDate(LocalDateTime.parse(newEventDto.getEventDate(), formatter))
                .initiator(user)
                .location(location)
                .requestModeration(newEventDto.getRequestModeration())
                .state(EventState.PENDING)
                .title(newEventDto.getTitle())
                .build();
        event.setPaid(newEventDto.getPaid() != null ? newEventDto.getPaid() : false);
        event.setParticipantLimit(newEventDto.getParticipantLimit() != null ? newEventDto.getParticipantLimit() : 0L);
        event.setRequestModeration(newEventDto.getRequestModeration() != null ? newEventDto.getRequestModeration() : false);
        event.setCommentModeration(newEventDto.getCommentModeration() != null ? newEventDto.getCommentModeration() : false);
        return event;
    }

    public static EventFullDto toEventFullDto(Event event, Long views) {
        EventFullDto eventFullDto = EventFullDto.builder()
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
                .requestModeration(event.getRequestModeration())
                .state(event.getState().toString())
                .title(event.getTitle())
                .commentModeration(event.getCommentModeration())
                .build();
        if (event.getPublishedOn() != null) eventFullDto.setPublishedOn(event.getPublishedOn().format(formatter));
        eventFullDto.setViews(views != null ? views : 0);
        return eventFullDto;
    }

    public static EventDtoWithComments toEventDtoWithComments(Event event, Long views, List<CommentDtoForEvent> comment) {
        EventDtoWithComments eventWithCommentsDto = EventDtoWithComments.builder()
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
                .requestModeration(event.getRequestModeration())
                .state(event.getState().toString())
                .title(event.getTitle())
                .commentModeration(event.getCommentModeration())
                .build();
        eventWithCommentsDto.setComments(comment != null ? comment : new ArrayList<>());
        if (event.getPublishedOn() != null) eventWithCommentsDto.setPublishedOn(event.getPublishedOn().format(formatter));
        eventWithCommentsDto.setViews(views != null ? views : 0);
        return eventWithCommentsDto;
    }
}
