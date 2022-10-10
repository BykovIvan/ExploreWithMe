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
@Transactional
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final LocationRepository locationRepository;
    private final StatClient statClient;

    @Override
    public List<EventShortDto> getAllForAllUsers(String text, Long[] categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size) {
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
        //TODO
//        Boolean onlyAvailable,

        //TODO сохранение в базу статистики

        //TODO
        Long views = 1L;
        //TODO
//        "EVENT_DATE" и "VIEWS" сделать только по stream
        //сделать запрос в бд
        if (sort.equals("EVENT_DATE")) {
            Page<Event> getList = eventRepository.findByParamFromUser(text, categories, paid, state, start, end, FromSizeSortPageable.of(from, size, Sort.by(Sort.Direction.ASC, "eventDate")));
            if (onlyAvailable) {
                return getList.stream()
                        .filter((Event event) -> event.getConfirmedRequests() <= event.getParticipantLimit() || event.getParticipantLimit() == 0) // ParticipantLimit = 0 - означает отсутствие ограничения
                        .map((Event event) -> EventMapper.toEventShortDto(event, views))
                        .collect(Collectors.toList());
            }
            return getList.stream()
                    .map((Event event) -> EventMapper.toEventShortDto(event, views))
                    .collect(Collectors.toList());
        } else if (sort.equals("VIEWS")) {
            Page<Event> getList = eventRepository.findByParamFromUser(text, categories, paid, state, start, end, FromSizeSortPageable.of(from, size, Sort.by(Sort.Direction.ASC, "id")));
            if (onlyAvailable) {
                return getList.stream()
                        .filter((Event event) -> event.getConfirmedRequests() <= event.getParticipantLimit() || event.getParticipantLimit() == 0)
                        .map((Event event) -> EventMapper.toEventShortDto(event, views))
                        .sorted(Comparator.comparingLong(EventShortDto::getViews))
                        .collect(Collectors.toList());
            }
            return getList.stream()
                    .map((Event event) -> EventMapper.toEventShortDto(event, views))
                    .sorted(Comparator.comparingLong(EventShortDto::getViews))
                    .collect(Collectors.toList());
        } else {
            throw new ValidationException(Event.class, "sort = " + sort, "Введены неверные параметры!");
        }
    }

    @Override
    public EventFullDto getByIdForAllUsers(Long eventId) {
        //TODO добавить проверки для выставления ошибок
        return EventMapper.toEventDto(eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString())));
    }


    //путь Для users/
    @Override
    public List<EventShortDto> findByUserIdFromUser(Long userId, String remoteAddr, String requestURI, Integer from, Integer size) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", userId.toString()));
        //запись в сервис статистики
        //Сохранять статистику нужно будет по двум эндпоинтам:
        // GET /events, который отвечает за получение событий с возможностью фильтрации, и
        // GET /events/{id}, который позволяет получить подробную информацию об опубликованном событии по его идентификатору
        StatisticDto statisticDto = StatisticDto.builder()
                .app("EventService")
                .uri(requestURI)
                .ip(remoteAddr)
                .timestamp(LocalDateTime.now().format(formatter))
                .build();
        statClient.createStat(statisticDto);
        //виюшка должна быть у конкретного события????
        Long views = null;
        //добавить views
        return eventRepository.findAllByInitiatorId(userId, FromSizeSortPageable.of(from, size, Sort.by(Sort.Direction.ASC, "id")))
                .stream()
                .map((Event event) -> EventMapper.toEventShortDto(event, views))
                //то что правильно :
//                .map((Event event) -> EventMapper.toEventShortDto(event, statClient.getViewByIdEvent(event.getId())))
                .collect(Collectors.toList());
    }

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
        //TODO
//        Long views = statClient.getViewByIdEvent(event.getId()));
        Long views = 0L;
        return EventMapper.toEventFullDto(eventRepository.save(event), views);
    }

    @Override
    public EventFullDto createFromUser(Long userId, NewEventDto newEventDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", userId.toString()));
        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(() -> new EntityNotFoundException(Category.class, "id", newEventDto.getCategory().toString()));
        Location location = locationRepository.save(LocationMapper.toLocation(newEventDto.getLocation()));
        Event event = EventMapper.toEvent(newEventDto, category, user, location);
        event.setState(StateOfEventAndReq.PENDING);
        //TODO
//        Long views = statClient.getViewByIdEvent(event.getId()));
        Long views = 0L;
        Event eventNew = eventRepository.save(event);
        return EventMapper.toEventFullDto(eventNew, views);
    }

    @Override
    public EventFullDto findByUserIdAndEventIdFromUser(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", userId.toString()));
        eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString()));
        //TODO
//        Long views = statClient.getViewByIdEvent(event.getId()));
        Long views = 0L;
        return EventMapper.toEventFullDto(eventRepository.findByIdAndInitiatorId(eventId, userId), views);
    }

    @Override
    public EventFullDto canselByUserIdAndEventIdFromUser(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", userId.toString()));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString()));
        if (event.getState().equals(StateOfEventAndReq.PUBLISHED)) {
            throw new ValidationException(Event.class, "State = " + event.getState(), "Событие уже опубликовано!");
        }
        event.setState(StateOfEventAndReq.CANCELED);
        //TODO
//        Long views = statClient.getViewByIdEvent(event.getId()));
        Long views = 0L;
        return EventMapper.toEventFullDto(eventRepository.save(event), views);
    }

    @Override
    public List<ParticipationRequestDto> findRequestsByUserIdAndEventIdFromUser(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", userId.toString()));
        eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString()));
        return requestRepository.findByEventAndRequester(eventId, userId)
                .stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    //если для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется
    //нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие
    //если при подтверждении данной заявки, лимит заявок для события исчерпан, то все неподтверждённые заявки необходимо отклонить

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

    @Override
    public List<EventFullDto> getByParamFromAdmin(Long[] users, String[] states, Long[] categories, String rangeStart, String rangeEnd, Integer from, Integer size) {
        Long views = 0L;
        StateOfEventAndReq[] stateOfEvent = new StateOfEventAndReq[states.length];
        for (int i = 0; i < states.length; i++) {
            stateOfEvent[i] = StateOfEventAndReq.valueOf(states[i]);
        }
        LocalDateTime start = LocalDateTime.parse(rangeStart, formatter);
        LocalDateTime end = LocalDateTime.parse(rangeEnd, formatter);
        return eventRepository.findByParamFromAdmin(users, stateOfEvent, categories, start, end, FromSizeSortPageable.of(from, size, Sort.by(Sort.Direction.ASC, "id")))
                .stream()
//                .map((Event event) -> EventMapper.toEventFullDto(event, client.getViews(event.getId())
                .map((Event event) -> EventMapper.toEventFullDto(event, views))
                .collect(Collectors.toList());
        //TODO сделать запрос с параметрами
    }

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
        //TODO сделать запрос клиента на количество просмотров
        Long views = null;

        return EventMapper.toEventFullDto(eventRepository.save(event), views);
    }

    // Дата начала события должна быть не ранее чем за час от даты публикации.
    // Событие должно быть в состоянии ожидания публикации
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
        //TODO сделать запрос клиента на количество просмотров
        Long views = null;
        return EventMapper.toEventFullDto(eventRepository.save(event), views);
    }

    //Событие не должно быть опубликовано

    @Override
    public EventFullDto rejectEventByIdFromAdmin(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString()));
        if (event.getState().equals(StateOfEventAndReq.PUBLISHED)) {
            throw new ValidationException(Event.class, "state = " + event.getState(), "Событие уже опубликовано!");
        }
        event.setState(StateOfEventAndReq.CANCELED);
        //TODO сделать запрос клиента на количество просмотров
        Long views = null;
        return EventMapper.toEventFullDto(eventRepository.save(event), views);
    }

}
