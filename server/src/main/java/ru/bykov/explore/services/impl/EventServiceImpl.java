package ru.bykov.explore.services.impl;

import io.micrometer.core.instrument.Statistic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bykov.explore.clientstat.StatClient;
import ru.bykov.explore.clientstat.StatisticDto;
import ru.bykov.explore.exceptions.NotFoundException;
import ru.bykov.explore.model.Event;
import ru.bykov.explore.model.User;
import ru.bykov.explore.model.dto.event.EventDto;
import ru.bykov.explore.model.dto.event.EventRequestDto;
import ru.bykov.explore.model.dto.event.EventShortDto;
import ru.bykov.explore.repositories.EventRepository;
import ru.bykov.explore.repositories.UserRepository;
import ru.bykov.explore.services.EventService;
import ru.bykov.explore.utils.mappingForDto.EventMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    private final StatClient statClient;


    @Override
    public List<EventShortDto> getAllForAllUsers() {
        return eventRepository.findAll().stream()
                .map(EventMapping::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventShortDto getByIdForAllUsers(Long eventId) {
        return EventMapping.toEventShortDto(eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Нет такого события!")));
    }

    @Override
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
    public List<EventRequestDto> findRequestsByUserIdAndEventId(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет такого пользователя!"));
        return null;
    }

    @Override
    public EventRequestDto confirmRequestByUserIdAndEventId(Long userId, Long eventId, Long reqId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет такого пользователя!"));
        return null;
    }

    @Override
    public EventRequestDto rejectRequestByUserIdAndEventId(Long userId, Long eventId, Long reqId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет такого пользователя!"));
        return null;
    }
}
