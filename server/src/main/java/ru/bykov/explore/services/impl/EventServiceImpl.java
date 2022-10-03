package ru.bykov.explore.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.bykov.explore.clientstat.StatClient;
import ru.bykov.explore.clientstat.StatisticDto;
import ru.bykov.explore.exceptions.NoParamInRequestException;
import ru.bykov.explore.exceptions.NotFoundException;
import ru.bykov.explore.model.Event;
import ru.bykov.explore.model.User;
import ru.bykov.explore.model.dto.ParticipationRequestDto;
import ru.bykov.explore.model.dto.event.EventFullDto;
import ru.bykov.explore.model.dto.event.EventShortDto;
import ru.bykov.explore.model.dto.event.NewEventDto;
import ru.bykov.explore.model.dto.event.UpdateEventRequest;
import ru.bykov.explore.repositories.CategoryRepository;
import ru.bykov.explore.repositories.EventRepository;
import ru.bykov.explore.repositories.UserRepository;
import ru.bykov.explore.services.EventService;
import ru.bykov.explore.utils.FromSizeSortPageable;
import ru.bykov.explore.utils.StateOfEvent;
import ru.bykov.explore.utils.mapperForDto.EventMapper;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
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
    public EventFullDto updateByUserIdFromUser(Long userId, UpdateEventRequest updateEventRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет такого пользователя!"));

        //TODO сделать проверка на то что человек ранее именно он опубликовал данную запись
        Event event = eventRepository.findById(updateEventRequest.getEventId()).orElseThrow(() -> new NotFoundException("Такого события не существует!"));
        if (event.getState().equals(StateOfEvent.PUBLISHED)){
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

        if (event.getState().equals(StateOfEvent.CANCELED)){
            event.setState(StateOfEvent.PENDING);
        }
        return null;
//        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto createByUserId(Long userId, EventFullDto eventFullDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет такого пользователя!"));
        return null;
    }

    @Override
    public EventFullDto findByUserIdAndEventId(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет такого пользователя!"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Нет такого события!"));

        return null;
    }

    @Override
    public EventFullDto canselByUserIdAndEventId(Long userId, Long eventId, EventFullDto eventFullDto1) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет такого пользователя!"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Нет такого события!"));
        return null;
    }

    @Override
    public List<ParticipationRequestDto> findRequestsByUserIdAndEventId(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет такого пользователя!"));
        return null;
    }

    @Override
    public ParticipationRequestDto confirmRequestByUserIdAndEventId(Long userId, Long eventId, Long reqId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет такого пользователя!"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Нет такого события!"));
        return null;
    }

    @Override
    public ParticipationRequestDto rejectRequestByUserIdAndEventId(Long userId, Long eventId, Long reqId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет такого пользователя!"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Нет такого события!"));
        return null;
    }

    @Override
    public List<EventFullDto> getByParamFromAdmin(Long[] users, String[] states, Long[] categories, String rangeStart, String rangeEnd, Integer from, Integer size) {

        return null;
    }

    @Override
    public EventFullDto updateByIdFromAdmin(Long eventId, NewEventDto newEventDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Нет такого события!"));

        return null;
    }

    @Override
    public EventFullDto publishEventByIdFromAdmin(Long eventId) {
        //дата начала события должна быть не ранее чем за час от даты публикации.
        //событие должно быть в состоянии ожидания публикации
        return null;
    }

    @Override
    public EventFullDto rejectEventByIdFromAdmin(Long eventId) {
        return null;
    }

}
