package ru.bykov.explore.services;

import ru.bykov.explore.model.dto.event.EventFullDto;
import ru.bykov.explore.model.dto.event.NewEventDto;
import ru.bykov.explore.model.dto.ParticipationRequestDto;
import ru.bykov.explore.model.dto.event.EventShortDto;

import java.util.List;

public interface EventService {

    List<EventShortDto> getAllForAllUsers(String text, String[] categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size);

    EventFullDto getByIdForAllUsers(Long eventId);

    //Получение событий, добавленых текущим пользователем
//    List<EventDto> findByUserId(Long userId);
    List<EventFullDto> findByUserId(Long userId, String remoteAddr, String requestURI);

    EventFullDto updateByUserId(Long userId, EventFullDto eventFullDto);

    EventFullDto createByUserId(Long userId, EventFullDto eventFullDto);

    EventFullDto findByUserIdAndEventId(Long userId, Long eventId);

    EventFullDto canselByUserIdAndEventId(Long userId, Long eventId, EventFullDto eventFullDto1);

    List<ParticipationRequestDto> findRequestsByUserIdAndEventId(Long userId, Long eventId);

    ParticipationRequestDto confirmRequestByUserIdAndEventId(Long userId, Long eventId, Long reqId);

    ParticipationRequestDto rejectRequestByUserIdAndEventId(Long userId, Long eventId, Long reqId);

    List<EventFullDto> getByParamFromAdmin(Long[] users, String[] states, Long[] categories, String rangeStart, String rangeEnd, Integer from, Integer size);

    EventFullDto updateByIdFromAdmin(Long eventId, NewEventDto newEventDto);

    EventFullDto publishEventByIdFromAdmin(Long eventId);

    EventFullDto rejectEventByIdFromAdmin(Long eventId);

}
