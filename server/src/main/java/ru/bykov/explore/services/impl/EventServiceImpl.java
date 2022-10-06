package ru.bykov.explore.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bykov.explore.clientstat.StatClient;
import ru.bykov.explore.clientstat.StatisticDto;
import ru.bykov.explore.exceptions.NoParamInRequestException;
import ru.bykov.explore.exceptions.NotFoundException;
import ru.bykov.explore.model.Category;
import ru.bykov.explore.model.Event;
import ru.bykov.explore.model.Request;
import ru.bykov.explore.model.User;
import ru.bykov.explore.model.dto.ParticipationRequestDto;
import ru.bykov.explore.model.dto.event.*;
import ru.bykov.explore.repositories.CategoryRepository;
import ru.bykov.explore.repositories.EventRepository;
import ru.bykov.explore.repositories.RequestRepository;
import ru.bykov.explore.repositories.UserRepository;
import ru.bykov.explore.services.EventService;
import ru.bykov.explore.utils.FromSizeSortPageable;
import ru.bykov.explore.utils.StateOfEventAndReq;
import ru.bykov.explore.utils.mapperForDto.EventMapper;
import ru.bykov.explore.utils.mapperForDto.LocationMapper;
import ru.bykov.explore.utils.mapperForDto.RequestMapper;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final StatClient statClient;

    @Override
    public List<EventShortDto> getAllForAllUsers(String text, String[] categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size) {
        if (text == null || categories == null || paid == null || rangeStart == null || rangeEnd == null) {
            throw new NoParamInRequestException("Плохо составленый запрос!");
        }
        if (from < 0 || size <= 0) {
            throw new NoParamInRequestException("Введены неверные параметры!");
        }
        if (!sort.equals("EVENT_DATE") || !sort.equals("VIEWS")) {
            throw new NoParamInRequestException("Введены неверные параметры!");
        }
        //TODO
        Long views = null;
        //сделать запрос в бд
        return eventRepository.findByParam(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, FromSizeSortPageable.of(from, size, Sort.by(Sort.Direction.DESC, sort)))
                .stream()
                .map((Event event) -> EventMapper.toEventShortDto(event, views))
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getByIdForAllUsers(Long eventId) {
        //добавить проверки для выставления ошибок
        return EventMapper.toEventDto(eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Нет такого события!")));
    }


    //путь Для users/
    @Override
    public List<EventShortDto> findByUserIdFromUser(Long userId, String remoteAddr, String requestURI, Integer from, Integer size) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет такого пользователя!"));
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
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет такого пользователя!"));

        //TODO сделать проверка на то что человек ранее именно он опубликовал данную запись
        Event event = eventRepository.findById(updateEventRequest.getEventId()).orElseThrow(() -> new NotFoundException("Такого события не существует!"));
        if (event.getInitiator().getId() != user.getId()){
            throw new NoParamInRequestException("Пользователь не является инициатором данного события!");
        }
        if (event.getState().equals(StateOfEventAndReq.PUBLISHED)){
            throw new NoParamInRequestException("Событие уже опубликовано и его нельзя изменить!");
        }

        event.setAnnotation(updateEventRequest.getAnnotation());
        if (updateEventRequest.getCategory() != null) {
            event.setCategory(categoryRepository.findById(updateEventRequest.getCategory()).orElseThrow(() -> new NotFoundException("Такого категории не существует!")));
        }
        event.setDescription(updateEventRequest.getDescription());
        if (updateEventRequest.getEventDate() != null){
            LocalDateTime dateAndTimeOfEvent = LocalDateTime.parse(updateEventRequest.getEventDate(), formatter);
            Duration duration = Duration.between(LocalDateTime.now(), dateAndTimeOfEvent);
            if (duration.toMinutes() < 120){
                throw new NoParamInRequestException("Время до события менее 2 часов и поэтому его нельзя изменить!");
            }
            event.setEventDate(dateAndTimeOfEvent);
        }
        if (updateEventRequest.getPaid() != null){
            event.setPaid(updateEventRequest.getPaid());
        }
        event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        event.setTitle(updateEventRequest.getTitle());
        if (event.getState().equals(StateOfEventAndReq.CANCELED)){
            event.setState(StateOfEventAndReq.PENDING);
        }
        //TODO
//        Long views = statClient.getViewByIdEvent(event.getId()));
        Long views = 0L;
        return EventMapper.toEventFullDto(eventRepository.save(event), views);
    }

    @Override
    public EventFullDto createFromUser(Long userId, NewEventDto newEventDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет такого пользователя!"));
        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(() -> new NotFoundException("Нет такой категории!"));
        Event event = EventMapper.toEvent(newEventDto, category, user);
        //TODO
//        Long views = statClient.getViewByIdEvent(event.getId()));
        Long views = 0L;
        return EventMapper.toEventFullDto(eventRepository.save(event), views);
    }

    @Override
    public EventFullDto findByUserIdAndEventIdFromUser(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет такого пользователя!"));
        eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Нет такого события!"));
        //TODO
//        Long views = statClient.getViewByIdEvent(event.getId()));
        Long views = 0L;
        return EventMapper.toEventFullDto(eventRepository.findByIdAndInitiatorId(eventId, userId), views);
    }

    @Override
    public EventFullDto canselByUserIdAndEventIdFromUser(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет такого пользователя!"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Нет такого события!"));
        if (event.getState().equals(StateOfEventAndReq.PUBLISHED)){
            throw new NoParamInRequestException("Событие уже опубликовано!");
        }
        event.setState(StateOfEventAndReq.CANCELED);
        //TODO
//        Long views = statClient.getViewByIdEvent(event.getId()));
        Long views = 0L;
        return EventMapper.toEventFullDto(eventRepository.save(event), views);
    }

    @Override
    public List<ParticipationRequestDto> findRequestsByUserIdAndEventIdFromUser(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет такого пользователя!"));
        eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Нет такого события!"));
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
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет такого пользователя!"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Нет такого события!"));
        Request request = requestRepository.findById(reqId).orElseThrow(() -> new NotFoundException("Нет такого запроса!"));
        if (event.getInitiator().getId() != userId){
            throw new NoParamInRequestException("Пользователь не является инициатором события!");
        }
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()){
            //TODO Проверить
            //если для события отключена пре-модерация запросов на участие, то запрос должен автоматически перейти в состояние подтвержденного
//            throw new NoParamInRequestException("Подтверждение заявки не требуется!");
            request.setStatus(StateOfEventAndReq.PUBLISHED);
            return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
        }
        if (requestRepository.countByEvent(eventId) >= event.getParticipantLimit()){
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
        //можно вставить метод проверки на все правила что бы не было дубликата!
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Нет такого события!"));
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет такого пользователя!"));
        Request request = requestRepository.findById(reqId).orElseThrow(() -> new NotFoundException("Нет такого запроса!"));
        if (event.getInitiator().getId() != userId){
            throw new NoParamInRequestException("Пользователь не является инициатором события!");
        }
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()){
            throw new NoParamInRequestException("Подтверждение заявки не требуется!");
        }
        if (request.getStatus().equals(StateOfEventAndReq.PUBLISHED)){
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            eventRepository.save(event);
        }
        request.setStatus(StateOfEventAndReq.CANCELED);
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public List<EventFullDto> getByParamFromAdmin(Long[] users, String[] states, Long[] categories, String rangeStart, String rangeEnd, Integer from, Integer size) {

        return null;
    }

    @Override
    public EventFullDto updateByIdFromAdmin(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Нет такого события!"));
        if (adminUpdateEventRequest.getAnnotation() != null){
            event.setAnnotation(adminUpdateEventRequest.getAnnotation());
        }
        if (adminUpdateEventRequest.getCategory() != null){
            event.setCategory(categoryRepository.findById(adminUpdateEventRequest.getCategory()).orElseThrow(() -> new NotFoundException("Нет такой категории!")));
        }
        if (adminUpdateEventRequest.getDescription() != null){
            event.setDescription(adminUpdateEventRequest.getDescription());
        }
        if (adminUpdateEventRequest.getEventDate() != null){
            event.setEventDate(LocalDateTime.parse(adminUpdateEventRequest.getEventDate(), formatter));
        }
        if (adminUpdateEventRequest.getLocation() != null){
            event.setLocation(LocationMapper.toLocation(adminUpdateEventRequest.getLocation()));
        }
        if (adminUpdateEventRequest.getPaid() != null){
            event.setPaid(adminUpdateEventRequest.getPaid());
        }
        if (adminUpdateEventRequest.getParticipantLimit() != null){
            event.setParticipantLimit(adminUpdateEventRequest.getParticipantLimit());
        }
        if (adminUpdateEventRequest.getRequestModeration() != null){
            event.setRequestModeration(adminUpdateEventRequest.getRequestModeration());
        }
        if (adminUpdateEventRequest.getTitle() != null){
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
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Нет такого события!"));
        LocalDateTime dateAndTimeOfEvent = event.getEventDate();
        Duration duration = Duration.between(LocalDateTime.now(), dateAndTimeOfEvent);
        event.setPublishedOn(LocalDateTime.now());
        if (duration.toMinutes() < 60){
            throw new NoParamInRequestException("Дата начала события должна быть не ранее чем за час от даты публикации");
        }


        return null;
    }

    //Событие не должно быть опубликовано

    @Override
    public EventFullDto rejectEventByIdFromAdmin(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Нет такого события!"));


        return null;
    }

}
