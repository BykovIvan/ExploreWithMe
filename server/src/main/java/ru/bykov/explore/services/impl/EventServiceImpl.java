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
import ru.bykov.explore.utils.FromSizeSortPageable;
import ru.bykov.explore.utils.StateOfEventAndReq;
import ru.bykov.explore.utils.mapperForDto.EventMapper;
import ru.bykov.explore.utils.mapperForDto.LocationMapper;
import ru.bykov.explore.utils.mapperForDto.RequestMapper;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final LocationRepository locationRepository;
    private final StatClient statClient;

    //путь для не users
    @Transactional
    @Override
    public List<EventShortDto> findAllForAllUsers(String text, Long[] categories, Boolean paid, String rangeStart,
                                                  String rangeEnd, Boolean onlyAvailable, String sort, Integer from,
                                                  Integer size, String remoteAddr, String requestURI) {
        if (from < 0 || size <= 0) {
            throw new ValidationException(Event.class, "from = " + from + ", size = " + size, "Введены неверные параметры!");
        }
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (rangeStart == null || rangeEnd == null) {
            start = LocalDateTime.now();
        } else {
            start = LocalDateTime.parse(rangeStart, formatter);
            end = LocalDateTime.parse(rangeEnd, formatter);
        }
        StateOfEventAndReq state = StateOfEventAndReq.PUBLISHED;
        StatisticDto statisticDto = StatisticDto.builder()
                .app("Explore With Me App")
                .uri(requestURI)
                .ip(remoteAddr)
                .build();
        statClient.createStat(statisticDto);
        //TODO stat
        if (sort.equals("EVENT_DATE")) {
            Page<Event> getList = eventRepository.findByParamFromUser(text, categories, paid, state, start, end, FromSizeSortPageable.of(from, size, Sort.by(Sort.Direction.ASC, "eventDate")));
            if (onlyAvailable) {
                return getList.stream()
                        .filter((Event event) -> event.getConfirmedRequests() < event.getParticipantLimit() || event.getParticipantLimit() == 0) // ParticipantLimit = 0 - означает отсутствие ограничения
                        .map((Event event) -> EventMapper.toEventShortDto(event, statClient.getViewsOfEvent("Explore With Me App", "/events/" + event.getId()).getBody().getViews()))
                        .collect(Collectors.toList());
            }
            return getList.stream()
                    .map((Event event) -> EventMapper.toEventShortDto(event, statClient.getViewsOfEvent("Explore With Me App", "/events/" + event.getId()).getBody().getViews()))
                    .collect(Collectors.toList());
        } else if (sort.equals("VIEWS")) {
            Page<Event> getList = eventRepository.findByParamFromUser(text, categories, paid, state, start, end, FromSizeSortPageable.of(from, size, Sort.by(Sort.Direction.ASC, "id")));
            if (onlyAvailable) {
                return getList.stream()
                        .filter((Event event) -> event.getConfirmedRequests() < event.getParticipantLimit() || event.getParticipantLimit() == 0)
                        .map((Event event) -> EventMapper.toEventShortDto(event, statClient.getViewsOfEvent("Explore With Me App", "/events/" + event.getId()).getBody().getViews()))
                        .sorted(Comparator.comparingLong(EventShortDto::getViews))
                        .collect(Collectors.toList());
            }
            return getList.stream()
                    .map((Event event) -> EventMapper.toEventShortDto(event, statClient.getViewsOfEvent("Explore With Me App", "/events/" + event.getId()).getBody().getViews()))
                    .sorted(Comparator.comparingLong(EventShortDto::getViews))
                    .collect(Collectors.toList());
        } else {
            throw new ValidationException(Event.class, "sort = " + sort, "Введены неверные параметры!");
        }
    }

    @Transactional
    @Override
    public EventFullDto findByIdForAllUsers(Long eventId, String remoteAddr, String requestURI) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString()));
        if (!event.getState().equals(StateOfEventAndReq.PUBLISHED)) {
            throw new ValidationException(Event.class, "State = " + event.getState(), "Событие не опубликовано!");
        }
        statClient.createStat(StatisticDto.builder()
                .app("Explore With Me App")
                .uri(requestURI)
                .ip(remoteAddr)
                .build());
//        ViewsDto viewsDto = statClient.getViewsOfEvent("Explore With Me App", "/events/" + event.getId()).getBody();
//        Long views = null;
//        if (viewsDto != null) {
//            views = viewsDto.getViews();
//        }
        return EventMapper.toEventFullDto(event, statClient.getViewsOfEvent("Explore With Me App", "/events/" + event.getId()).getBody().getViews());
    }

    //путь для users
    @Transactional
    @Override
    public List<EventShortDto> findByUserIdFromUser(Long userId, Integer from, Integer size) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", userId.toString()));
        return eventRepository.findAllByInitiatorId(userId, FromSizeSortPageable.of(from, size, Sort.by(Sort.Direction.ASC, "id")))
                .stream()
                .map((Event event) -> EventMapper.toEventShortDto(event, statClient.getViewsOfEvent("Explore With Me App", "/events/" + event.getId()).getBody().getViews()))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto updateFromUser(Long userId, UpdateEventRequest updateEventRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", userId.toString()));

        //TODO сделать проверка на то что человек ранее именно он опубликовал данную запись
        Event event = eventRepository.findById(updateEventRequest.getEventId()).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", updateEventRequest.getEventId().toString()));
        if (event.getInitiator().getId() != user.getId()) {
            throw new ValidationException(Event.class, "Initiator = " + event.getInitiator(), "Пользователь не является инициатором данного события!");
        }
        if (event.getState().equals(StateOfEventAndReq.PUBLISHED)) {
            throw new ValidationException(Event.class, "State = " + event.getState(), "Событие уже опубликовано и его нельзя изменить!");
        }
        event.setAnnotation(updateEventRequest.getAnnotation());
        if (updateEventRequest.getCategory() != null) {
            event.setCategory(categoryRepository.findById(updateEventRequest.getCategory()).orElseThrow(() -> new EntityNotFoundException(Category.class, "id", updateEventRequest.getCategory().toString())));
        }
        event.setDescription(updateEventRequest.getDescription());
        if (updateEventRequest.getEventDate() != null) {
            LocalDateTime dateAndTimeOfEvent = LocalDateTime.parse(updateEventRequest.getEventDate(), formatter);
            Duration duration = Duration.between(LocalDateTime.now(), dateAndTimeOfEvent);
            if (duration.toMinutes() < 120) {
                throw new ValidationException(Event.class, "EventDate = " + event.getEventDate(), "Время до события менее 2 часов и поэтому его нельзя изменить!");
            }
            event.setEventDate(dateAndTimeOfEvent);
        }
        if (updateEventRequest.getPaid() != null) {
            event.setPaid(updateEventRequest.getPaid());
        }
        event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        event.setTitle(updateEventRequest.getTitle());
        if (event.getState().equals(StateOfEventAndReq.CANCELED)) {
            event.setState(StateOfEventAndReq.PENDING);
        }
        return EventMapper.toEventFullDto(eventRepository.save(event), statClient.getViewsOfEvent("Explore With Me App", "/events/" + event.getId()).getBody().getViews());
    }

    @Transactional
    @Override
    public EventFullDto createFromUser(Long userId, NewEventDto newEventDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", userId.toString()));
        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(() -> new EntityNotFoundException(Category.class, "id", newEventDto.getCategory().toString()));
        Location location = locationRepository.save(LocationMapper.toLocation(newEventDto.getLocation()));
        Event event = EventMapper.toEvent(newEventDto, category, user, location);
        event.setState(StateOfEventAndReq.PENDING);
        Event eventNew = eventRepository.save(event);
        return EventMapper.toEventFullDto(eventNew, 0L);
    }

    @Transactional
    @Override
    public EventFullDto findByUserIdAndEventIdFromUser(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", userId.toString()));
        eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString()));
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId);
        return EventMapper.toEventFullDto(event, statClient.getViewsOfEvent("Explore With Me App", "/events/" + event.getId()).getBody().getViews());
    }

    @Transactional
    @Override
    public EventFullDto canselByUserIdAndEventIdFromUser(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", userId.toString()));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString()));
        if (event.getState().equals(StateOfEventAndReq.PUBLISHED)) {
            throw new ValidationException(Event.class, "State = " + event.getState(), "Событие уже опубликовано!");
        }
        event.setState(StateOfEventAndReq.CANCELED);
        return EventMapper.toEventFullDto(eventRepository.save(event), statClient.getViewsOfEvent("Explore With Me App", "/events/" + event.getId()).getBody().getViews());
    }

    @Transactional
    @Override
    public List<ParticipationRequestDto> findRequestsByUserIdAndEventIdFromUser(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", userId.toString()));
        eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString()));
        return requestRepository.findByEventAndRequester(eventId, userId)
                .stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ParticipationRequestDto confirmRequestByUserIdAndEventIdFromUser(Long userId, Long eventId, Long reqId) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", userId.toString()));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString()));
        Request request = requestRepository.findById(reqId).orElseThrow(() -> new EntityNotFoundException(Request.class, "id", reqId.toString()));
        if (event.getInitiator().getId() != userId) {
            throw new ValidationException(Event.class, "Initiator = " + event.getInitiator(), "Пользователь не является инициатором события!");
        }
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            //TODO Проверить
            //если для события отключена пре-модерация запросов на участие, то запрос должен автоматически перейти в состояние подтвержденного
//            throw new NoParamInRequestException("Подтверждение заявки не требуется!");
            request.setStatus(StateOfEventAndReq.PUBLISHED);
            return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
        } else if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            //TODO Проверить запрос в таблице через запрос, будет ли работать!
            requestRepository.setStatusCanselWhereByStatusAndEventId(StateOfEventAndReq.CANCELED, StateOfEventAndReq.PENDING, eventId);
//            List<Request> listOfReq = requestRepository.findAllByEventAndStatus(eventId, StateOfEventAndReq.PENDING);
//            for (Request getRequester : listOfReq) {
//                getRequester.setStatus(StateOfEventAndReq.CANCELED);
//                requestRepository.save(getRequester);
//            }
//            request.setStatus(StateOfEventAndReq.CANCELED);
//            return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
        }
        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        eventRepository.save(event);
        request.setStatus(StateOfEventAndReq.PUBLISHED);
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Transactional
    @Override
    public ParticipationRequestDto rejectRequestByUserIdAndEventIdFromUser(Long userId, Long eventId, Long reqId) {
        //TODO можно вставить метод проверки на все правила что бы не было дубликата!
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString()));
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", userId.toString()));
        Request request = requestRepository.findById(reqId).orElseThrow(() -> new EntityNotFoundException(Request.class, "id", reqId.toString()));
        if (event.getInitiator().getId() != userId) {
            throw new ValidationException(Event.class, "Initiator = " + event.getInitiator(), "Пользователь не является инициатором события!");
        }
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            throw new ValidationException(Event.class, "ParticipantLimit = " + event.getParticipantLimit() + "RequestModeration = " + event.getParticipantLimit(), "Подтверждение заявки не требуется!");
        }
        if (request.getStatus().equals(StateOfEventAndReq.PUBLISHED)) {
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            eventRepository.save(event);
        }
        request.setStatus(StateOfEventAndReq.CANCELED);
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Transactional
    @Override
    public List<EventFullDto> findByParamFromAdmin(Long[] users, String[] states, Long[] categories, String rangeStart, String rangeEnd, Integer from, Integer size) {
        StateOfEventAndReq[] stateOfEvent = new StateOfEventAndReq[states.length];
        for (int i = 0; i < states.length; i++) {
            stateOfEvent[i] = StateOfEventAndReq.valueOf(states[i]);
        }
        LocalDateTime start = LocalDateTime.parse(rangeStart, formatter);
        LocalDateTime end = LocalDateTime.parse(rangeEnd, formatter);
        return eventRepository.findByParamFromAdmin(users, stateOfEvent, categories, start, end, FromSizeSortPageable.of(from, size, Sort.by(Sort.Direction.ASC, "id")))
                .stream()
                .map((Event event) -> EventMapper.toEventFullDto(event, statClient.getViewsOfEvent("Explore With Me App", "/events/" + event.getId()).getBody().getViews()))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto updateByIdFromAdmin(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString()));
        if (adminUpdateEventRequest.getAnnotation() != null) {
            event.setAnnotation(adminUpdateEventRequest.getAnnotation());
        }
        if (adminUpdateEventRequest.getCategory() != null) {
            event.setCategory(categoryRepository.findById(adminUpdateEventRequest.getCategory()).orElseThrow(() -> new EntityNotFoundException(Category.class, "id", adminUpdateEventRequest.getCategory().toString())));
        }
        if (adminUpdateEventRequest.getDescription() != null) {
            event.setDescription(adminUpdateEventRequest.getDescription());
        }
        if (adminUpdateEventRequest.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(adminUpdateEventRequest.getEventDate(), formatter));
        }
        if (adminUpdateEventRequest.getLocation() != null) {
            event.setLocation(LocationMapper.toLocation(adminUpdateEventRequest.getLocation()));
        }
        if (adminUpdateEventRequest.getPaid() != null) {
            event.setPaid(adminUpdateEventRequest.getPaid());
        }
        if (adminUpdateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(adminUpdateEventRequest.getParticipantLimit());
        }
        if (adminUpdateEventRequest.getRequestModeration() != null) {
            event.setRequestModeration(adminUpdateEventRequest.getRequestModeration());
        }
        if (adminUpdateEventRequest.getTitle() != null) {
            event.setTitle(adminUpdateEventRequest.getTitle());
        }
        return EventMapper.toEventFullDto(eventRepository.save(event), statClient.getViewsOfEvent("Explore With Me App", "/events/" + event.getId()).getBody().getViews());
    }

    @Transactional
    @Override
    public EventFullDto publishEventByIdFromAdmin(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString()));
        LocalDateTime dateAndTimeOfEvent = event.getEventDate();
        LocalDateTime dateAndTimeNowPublish = LocalDateTime.now();
        if (dateAndTimeOfEvent.isBefore(dateAndTimeNowPublish)) {
            Duration duration = Duration.between(dateAndTimeOfEvent, dateAndTimeNowPublish);
            if (duration.toMinutes() > 60) {
                throw new ValidationException(Event.class, "EventDate = " + event.getEventDate(), "Дата начала события должна быть не ранее чем за час от даты публикации");
            }
        }
        if (event.getState().equals(StateOfEventAndReq.CANCELED)) {
            throw new ValidationException(Event.class, "state = " + event.getState(), "Событие уже отменено!");
        }
        event.setPublishedOn(dateAndTimeNowPublish);
        event.setState(StateOfEventAndReq.PUBLISHED);
        return EventMapper.toEventFullDto(eventRepository.save(event), statClient.getViewsOfEvent("Explore With Me App", "/events/" + event.getId()).getBody().getViews());
    }

    @Transactional
    @Override
    public EventFullDto rejectEventByIdFromAdmin(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString()));
        if (event.getState().equals(StateOfEventAndReq.PUBLISHED)) {
            throw new ValidationException(Event.class, "state = " + event.getState(), "Событие уже опубликовано!");
        }
        event.setState(StateOfEventAndReq.CANCELED);
        return EventMapper.toEventFullDto(eventRepository.save(event), statClient.getViewsOfEvent("Explore With Me App", "/events/" + event.getId()).getBody().getViews());
    }

}
