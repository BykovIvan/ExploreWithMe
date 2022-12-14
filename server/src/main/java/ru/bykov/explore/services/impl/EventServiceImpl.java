package ru.bykov.explore.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bykov.explore.clientstat.StatClient;
import ru.bykov.explore.clientstat.StatisticDto;
import ru.bykov.explore.exceptions.EntityNotFoundException;
import ru.bykov.explore.exceptions.ValidationException;
import ru.bykov.explore.model.*;
import ru.bykov.explore.model.dto.ParticipationRequestDto;
import ru.bykov.explore.model.dto.event.*;
import ru.bykov.explore.repositories.*;
import ru.bykov.explore.services.EventService;
import ru.bykov.explore.utils.CommentState;
import ru.bykov.explore.utils.EventState;
import ru.bykov.explore.utils.FromSizeSortPageable;
import ru.bykov.explore.utils.RequestState;
import ru.bykov.explore.utils.mapperForDto.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final LocationRepository locationRepository;
    private final CommentRepository commentRepository;
    private final StatClient statClient;

    //путь для не users
    @Override
    public List<EventShortDto> findAllForAllUsers(String text, Long[] categories, Boolean paid, String rangeStart,
                                                  String rangeEnd, Boolean onlyAvailable, String sort, Integer from,
                                                  Integer size, String remoteAddr, String requestURI) {
        if (from < 0 || size <= 0) {
            throw new ValidationException(Event.class, "from=", from + "! Введен неверный параметр! Или ", "size=", size + "! Введен неверный параметр!");
        }
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (rangeStart == null && rangeEnd == null) {
            start = LocalDateTime.now();
        } else if (rangeStart == null && rangeEnd != null) {
            start = LocalDateTime.now();
            end = LocalDateTime.parse(rangeEnd, formatter);
        } else if (rangeStart != null && rangeEnd == null) {
            start = LocalDateTime.parse(rangeStart, formatter);
        } else {
            start = LocalDateTime.parse(rangeStart, formatter);
            end = LocalDateTime.parse(rangeEnd, formatter);
        }
        if (sort.equals("EVENT_DATE")) {
            Page<Event> getList = eventRepository.findByParamFromUser(text, categories, paid, EventState.PUBLISHED,
                    start, end, FromSizeSortPageable.of(from, size, Sort.by(Sort.Direction.ASC, "eventDate")));
            for (Event event : getList) {
                statClient.createStat(StatisticDto.builder()
                        .app("Explore With Me App")
                        .uri(requestURI + "/" + event.getId())
                        .ip(remoteAddr)
                        .build());
            }
            if (onlyAvailable) {
                return getList.stream()
                        .filter((Event event) -> event.getConfirmedRequests() < event.getParticipantLimit() || event.getParticipantLimit() == 0) // ParticipantLimit = 0 - означает отсутствие ограничения
                        .map((Event event) -> EventMapper.toEventShortDto(event, statClient.getViews(event)))
                        .collect(Collectors.toList());
            }
            return getList.stream()
                    .map((Event event) -> EventMapper.toEventShortDto(event, statClient.getViews(event)))
                    .collect(Collectors.toList());

        } else if (sort.equals("VIEWS")) {
            Page<Event> getList = eventRepository.findByParamFromUser(text, categories, paid, EventState.PUBLISHED,
                    start, end, FromSizeSortPageable.of(from, size, Sort.by(Sort.Direction.ASC, "id")));
            for (Event event : getList) {
                statClient.createStat(StatisticDto.builder()
                        .app("Explore With Me App")
                        .uri(requestURI + "/" + event.getId())
                        .ip(remoteAddr)
                        .build());
            }
            if (onlyAvailable) {
                return getList.stream()
                        .filter((Event event) -> event.getConfirmedRequests() < event.getParticipantLimit() || event.getParticipantLimit() == 0)
                        .map((Event event) -> EventMapper.toEventShortDto(event, statClient.getViews(event)))
                        .sorted(Comparator.comparingLong(EventShortDto::getViews))
                        .collect(Collectors.toList());
            }
            return getList.stream()
                    .map((Event event) -> EventMapper.toEventShortDto(event, statClient.getViews(event)))
                    .sorted(Comparator.comparingLong(EventShortDto::getViews))
                    .collect(Collectors.toList());
        } else {
            throw new ValidationException(Event.class, "sort=", sort + "! Введены неверные параметры!");
        }
    }

    @Override
    public EventFullDto findByIdForAllUsers(Long eventId, String remoteAddr, String requestURI) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString()));
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ValidationException(Event.class, "State=", event.getState() + "! Событие не опубликовано!");
        }
        statClient.createStat(StatisticDto.builder()
                .app("Explore With Me App")
                .uri(requestURI)
                .ip(remoteAddr)
                .build());
        return EventMapper.toEventFullDto(event, statClient.getViews(event));
    }

    //путь для users
    @Override
    public List<EventShortDto> findByUserIdFromUser(Long userId, Integer from, Integer size) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", userId.toString()));
        return eventRepository.findAllByInitiatorId(userId, FromSizeSortPageable.of(from, size, Sort.by(Sort.Direction.ASC, "id")))
                .stream()
                .map((Event event) -> EventMapper.toEventShortDto(event, statClient.getViews(event)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto updateFromUser(Long userId, UpdateEventRequest updateEventRequest) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", userId.toString()));
        if (updateEventRequest.getEventId() == null)
            throw new ValidationException(Event.class, "id", "null! В запросе обязательно должен присутствовать id события!");
        Event event = eventRepository.findByIdAndInitiatorId(updateEventRequest.getEventId(), userId)
                .orElseThrow(() -> new EntityNotFoundException(Event.class, "id", updateEventRequest.getEventId().toString()));
        if (event.getState().equals(EventState.PUBLISHED))
            throw new ValidationException(Event.class, "State", event.getState() + "! Событие уже опубликовано и его нельзя изменить!");
        if (updateEventRequest.getAnnotation() != null) event.setAnnotation(updateEventRequest.getAnnotation());
        if (updateEventRequest.getCategory() != null)
            event.setCategory(categoryRepository.findById(updateEventRequest.getCategory())
                    .orElseThrow(() -> new EntityNotFoundException(Category.class, "id", updateEventRequest.getCategory().toString())));
        if (updateEventRequest.getDescription() != null) event.setDescription(updateEventRequest.getDescription());
        if (updateEventRequest.getEventDate() != null) {
            LocalDateTime dateAndTimeOfEvent = LocalDateTime.parse(updateEventRequest.getEventDate(), formatter);
            Duration duration = Duration.between(LocalDateTime.now(), dateAndTimeOfEvent);
            if (duration.toMinutes() < 120) {
                throw new ValidationException(Event.class, "EventDate", event.getEventDate() + "! Время до события менее 2 часов и поэтому его нельзя изменить!");
            }
            event.setEventDate(dateAndTimeOfEvent);
        }
        if (updateEventRequest.getPaid() != null) event.setPaid(updateEventRequest.getPaid());
        if (updateEventRequest.getParticipantLimit() != null)
            event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        if (updateEventRequest.getTitle() != null) event.setTitle(updateEventRequest.getTitle());
        if (event.getState().equals(EventState.CANCELED)) event.setState(EventState.PENDING);
        return EventMapper.toEventFullDto(eventRepository.save(event), statClient.getViews(event));
    }

    @Override
    @Transactional
    public EventFullDto createFromUser(Long userId, NewEventDto newEventDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", userId.toString()));
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new EntityNotFoundException(Category.class, "id", newEventDto.getCategory().toString()));
        Location location = locationRepository.save(LocationMapper.toLocation(newEventDto.getLocation()));
        @Valid Event event = EventMapper.toEvent(newEventDto, category, user, location);
        event.setState(EventState.PENDING);
        return EventMapper.toEventFullDto(eventRepository.save(event), statClient.getViews(event));
    }

    @Override
    public EventFullDto findByUserIdAndEventIdFromUser(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", userId.toString()));
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString()));
        return EventMapper.toEventFullDto(event, statClient.getViews(event));
    }

    @Override
    @Transactional
    public EventFullDto canselByUserIdAndEventIdFromUser(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", userId.toString()));
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString()));
        if (event.getState().equals(EventState.PUBLISHED) || event.getState().equals(EventState.CANCELED))
            throw new ValidationException(Event.class, "State", event.getState() + "! Событие уже опубликовано или уже отменено!");
        event.setState(EventState.CANCELED);
        return EventMapper.toEventFullDto(eventRepository.save(event), statClient.getViews(event));
    }

    @Override
    @Transactional
    public List<ParticipationRequestDto> findRequestsByUserIdAndEventIdFromUser(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", userId.toString()));
        eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString()));
        return requestRepository.findAllByEventId(eventId).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto confirmRequestByUserIdAndEventIdFromUser(Long userId, Long eventId, Long reqId) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", userId.toString()));
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString()));
        Request request = requestRepository.findById(reqId).orElseThrow(() -> new EntityNotFoundException(Request.class, "id", reqId.toString()));
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            request.setStatus(RequestState.CONFIRMED);
            eventRepository.setNewConfirmedRequestsPlusOne(eventId);
            return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
        } else if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            requestRepository.setStatusCanselWhereByStatusAndEventId(RequestState.CANCELED, RequestState.PENDING, eventId);
            request.setStatus(RequestState.CANCELED);
            return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
        }
        request.setStatus(RequestState.CONFIRMED);
        eventRepository.setNewConfirmedRequestsPlusOne(eventId);
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));

    }

    @Override
    @Transactional
    public ParticipationRequestDto rejectRequestByUserIdAndEventIdFromUser(Long userId, Long eventId, Long reqId) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", userId.toString()));
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString()));
        Request request = requestRepository.findByIdAndEventId(reqId, eventId).orElseThrow(() -> new EntityNotFoundException(Request.class, "id", reqId.toString()));
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            throw new ValidationException(Event.class, "ParticipantLimit", event.getParticipantLimit() + "! RequestModeration = " +
                    event.getParticipantLimit() + "! Подтверждение заявки не требуется!");
        }
        if (request.getStatus().equals(RequestState.CONFIRMED) && eventRepository.findById(request.getEvent().getId()).get().getConfirmedRequests() != 0)
            eventRepository.setNewConfirmedRequestsMinusOne(eventId);
        request.setStatus(RequestState.REJECTED);
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    //путь для admin
    @Override
    public List<EventFullDto> findByParamFromAdmin(Long[] users, String[] states, Long[] categories, String rangeStart, String rangeEnd, Integer from, Integer size) {
        EventState[] stateOfEvent = new EventState[states.length];
        for (int i = 0; i < states.length; i++) {
            stateOfEvent[i] = EventState.valueOf(states[i]);
        }
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (rangeStart != null) start = LocalDateTime.parse(decode(rangeStart), formatter);
        if (rangeEnd != null) end = LocalDateTime.parse(decode(rangeEnd), formatter);
        return eventRepository.findByParamFromAdmin(users, stateOfEvent, categories, start, end,
                        FromSizeSortPageable.of(from, size, Sort.by(Sort.Direction.ASC, "id"))).stream()
                .map((Event event) -> EventMapper.toEventFullDto(event, statClient.getViews(event)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto updateByIdFromAdmin(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString()));
        if (adminUpdateEventRequest.getAnnotation() != null)
            event.setAnnotation(adminUpdateEventRequest.getAnnotation());
        if (adminUpdateEventRequest.getCategory() != null)
            event.setCategory(categoryRepository.findById(adminUpdateEventRequest.getCategory())
                    .orElseThrow(() -> new EntityNotFoundException(Category.class, "id", adminUpdateEventRequest.getCategory().toString())));
        if (adminUpdateEventRequest.getDescription() != null)
            event.setDescription(adminUpdateEventRequest.getDescription());
        if (adminUpdateEventRequest.getEventDate() != null)
            event.setEventDate(LocalDateTime.parse(adminUpdateEventRequest.getEventDate(), formatter));
        if (adminUpdateEventRequest.getLocation() != null)
            event.setLocation(LocationMapper.toLocation(adminUpdateEventRequest.getLocation()));
        if (adminUpdateEventRequest.getPaid() != null) event.setPaid(adminUpdateEventRequest.getPaid());
        if (adminUpdateEventRequest.getParticipantLimit() != null)
            event.setParticipantLimit(adminUpdateEventRequest.getParticipantLimit());
        if (adminUpdateEventRequest.getRequestModeration() != null)
            event.setRequestModeration(adminUpdateEventRequest.getRequestModeration());
        if (adminUpdateEventRequest.getTitle() != null) event.setTitle(adminUpdateEventRequest.getTitle());
        if (adminUpdateEventRequest.getCommentModeration() != null)
            event.setCommentModeration(adminUpdateEventRequest.getCommentModeration());
        return EventMapper.toEventFullDto(eventRepository.save(event), statClient.getViews(event));
    }

    @Override
    @Transactional
    public EventFullDto publishEventByIdFromAdmin(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString()));
        if (event.getState().equals(EventState.CANCELED) || event.getState().equals(EventState.PUBLISHED))
            throw new ValidationException(Event.class, "State", event.getState() + "! Событие уже отменено или опубликовано!");
        LocalDateTime dateAndTimeNowPublish = LocalDateTime.now();
        Duration duration = Duration.between(dateAndTimeNowPublish, event.getEventDate());
        if (duration.toMinutes() < 60) throw new ValidationException(Event.class, "EventDate", event.getEventDate() +
                "! Дата начала события должна быть не ранее чем за час от даты публикации");
        event.setPublishedOn(dateAndTimeNowPublish);
        event.setState(EventState.PUBLISHED);
        return EventMapper.toEventFullDto(eventRepository.save(event), statClient.getViews(event));
    }

    @Override
    @Transactional
    public EventFullDto rejectEventByIdFromAdmin(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString()));
        if (event.getState().equals(EventState.PUBLISHED))
            throw new ValidationException(Event.class, "State", event.getState() + "! Событие уже опубликовано!");
        event.setState(EventState.CANCELED);
        return EventMapper.toEventFullDto(eventRepository.save(event), statClient.getViews(event));
    }

    @Override
    @Transactional
    public EventDtoWithComments findEventWithCommentsByEventIdFromUser(Long userId, Long eventId, Integer from, Integer size) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", userId.toString()));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString()));
        if (event.getState().equals(EventState.PENDING) || event.getState().equals(EventState.CANCELED))
            throw new ValidationException(Event.class, "state", event.getState() + "! Событие не опубликовано или отменено!");

        List<Comment> listOfComment = commentRepository.findByEventIdAndStatus(eventId, CommentState.PUBLISHED,
                FromSizeSortPageable.of(from, size, Sort.by(Sort.Direction.DESC, "id")));

        return EventMapper.toEventDtoWithComments(event, statClient.getViews(event), listOfComment.stream()
                .map((Comment comment) -> CommentMapper.toCommentDtoForEvent(comment, UserMapper.toUserShortDto(comment.getOwner())))
                .collect(Collectors.toList()));
    }

    private String decode(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
