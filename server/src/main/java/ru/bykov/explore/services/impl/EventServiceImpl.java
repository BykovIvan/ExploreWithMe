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
import ru.bykov.explore.model.dto.event.EventDto;
import ru.bykov.explore.model.dto.event.NewEventDto;
import ru.bykov.explore.model.dto.RequestDto;
import ru.bykov.explore.model.dto.event.EventShortDto;
import ru.bykov.explore.repositories.EventRepository;
import ru.bykov.explore.repositories.UserRepository;
import ru.bykov.explore.services.EventService;
import ru.bykov.explore.utils.FromSizeSortPageable;
import ru.bykov.explore.utils.mapperForDto.EventMapper;

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
    private final StatClient statClient;

    @Override
    public List<EventShortDto> getAllForAllUsers(String text, String[] categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size) {
        if (text == null || categories == null || paid == null || rangeStart == null || rangeEnd == null) {
            throw new NoParamInRequestException("Плохо составленый запрос!");
        }
        if (from < 0 || size <= 0) {
            throw new NoParamInRequestException("Введены неверные параметры!");
        }
        if (!sort.equals("EVENT_DATE") || !sort.equals("VIEWS")){
            throw new NoParamInRequestException("Введены неверные параметры!");
        }
        //сделать запрос в бд
        return eventRepository.findByParam(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, FromSizeSortPageable.of(from, size, Sort.by(Sort.Direction.DESC, sort)))
                .stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventDto getByIdForAllUsers(Long eventId) {
        //добавить проверки для выставления ошибок
        return EventMapper.toEventDto(eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Нет такого события!")));
    }

    @Override
    public List<EventDto> findByUserId(Long userId, String remoteAddr, String requestURI) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет такого пользователя!"));
        //Принимать в методе все параметры, делать из него СтатикДто
        StatisticDto statisticDto = StatisticDto.builder()
                .app("EventService")
                .uri(requestURI)
                .ip(remoteAddr)
                .timestamp(LocalDateTime.now().format(formatter))
                .build();
        statClient.createStat(statisticDto);
        return null;
    }

//    @Override
    public List<EventDto> findByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет такого пользователя!"));
        //Принимать в методе все параметры, делать из него СтатикДто
        StatisticDto statisticDto = StatisticDto.builder()
                .build();
        statClient.createStat(statisticDto);
        return null;
    }

    @Override
    public EventDto updateByUserId(Long userId, EventDto eventDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет такого пользователя!"));
        return null;
    }

    @Override
    public EventDto createByUserId(Long userId, EventDto eventDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет такого пользователя!"));
        return null;
    }

    @Override
    public EventDto findByUserIdAndEventId(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет такого пользователя!"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Нет такого события!"));

        return null;
    }

    @Override
    public EventDto canselByUserIdAndEventId(Long userId, Long eventId, EventDto eventDto1) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет такого пользователя!"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Нет такого события!"));
        return null;
    }

    @Override
    public List<RequestDto> findRequestsByUserIdAndEventId(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет такого пользователя!"));
        return null;
    }

    @Override
    public RequestDto confirmRequestByUserIdAndEventId(Long userId, Long eventId, Long reqId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет такого пользователя!"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Нет такого события!"));
        return null;
    }

    @Override
    public RequestDto rejectRequestByUserIdAndEventId(Long userId, Long eventId, Long reqId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет такого пользователя!"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Нет такого события!"));
        return null;
    }

    @Override
    public List<EventDto> getByParamFromAdmin(Long[] users, String[] states, Long[] categories, String rangeStart, String rangeEnd, Integer from, Integer size) {

        return null;
    }

    @Override
    public EventDto updateByIdFromAdmin(Long eventId, NewEventDto newEventDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Нет такого события!"));

        return null;
    }

    @Override
    public EventDto publishEventByIdFromAdmin(Long eventId) {
        //дата начала события должна быть не ранее чем за час от даты публикации.
        //событие должно быть в состоянии ожидания публикации
        return null;
    }

    @Override
    public EventDto rejectEventByIdFromAdmin(Long eventId) {
        return null;
    }

}
