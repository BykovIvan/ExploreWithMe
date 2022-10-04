package ru.bykov.explore.services;

import ru.bykov.explore.model.dto.event.EventFullDto;
import ru.bykov.explore.model.dto.event.NewEventDto;
import ru.bykov.explore.model.dto.ParticipationRequestDto;
import ru.bykov.explore.model.dto.event.EventShortDto;
import ru.bykov.explore.model.dto.event.UpdateEventRequest;

import java.util.List;

public interface EventService {

    List<EventShortDto> getAllForAllUsers(String text, String[] categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size);

    EventFullDto getByIdForAllUsers(Long eventId);

    //Получение событий, добавленных текущим пользователем
    List<EventShortDto> findByUserIdFromUser(Long userId, String remoteAddr, String requestURI, Integer from, Integer size);

    EventFullDto updateFromUser(Long userId, UpdateEventRequest updateEventRequest);

    EventFullDto createFromUser(Long userId, NewEventDto newEventDto);

    EventFullDto findByUserIdAndEventIdFromUser(Long userId, Long eventId);

    EventFullDto canselByUserIdAndEventIdFromUser(Long userId, Long eventId);

    List<ParticipationRequestDto> findRequestsByUserIdAndEventIdFromUser(Long userId, Long eventId);

    ParticipationRequestDto confirmRequestByUserIdAndEventId(Long userId, Long eventId, Long reqId);

    ParticipationRequestDto rejectRequestByUserIdAndEventId(Long userId, Long eventId, Long reqId);

    List<EventFullDto> getByParamFromAdmin(Long[] users, String[] states, Long[] categories, String rangeStart, String rangeEnd, Integer from, Integer size);

    EventFullDto updateByIdFromAdmin(Long eventId, NewEventDto newEventDto);

    EventFullDto publishEventByIdFromAdmin(Long eventId);

    EventFullDto rejectEventByIdFromAdmin(Long eventId);

}
