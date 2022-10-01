package ru.bykov.explore.services;

import ru.bykov.explore.model.dto.event.EventDto;
import ru.bykov.explore.model.dto.event.NewEventDto;
import ru.bykov.explore.model.dto.RequestDto;
import ru.bykov.explore.model.dto.event.EventShortDto;

import java.util.List;

public interface EventService {

    List<EventShortDto> getAllForAllUsers(String text, String[] categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size);

    EventDto getByIdForAllUsers(Long eventId);

    //Получение событий, добавленых текущим пользователем
//    List<EventDto> findByUserId(Long userId);
    List<EventDto> findByUserId(Long userId, String remoteAddr, String requestURI);

    EventDto updateByUserId(Long userId, EventDto eventDto);

    EventDto createByUserId(Long userId, EventDto eventDto);

    EventDto findByUserIdAndEventId(Long userId, Long eventId);

    EventDto canselByUserIdAndEventId(Long userId, Long eventId, EventDto eventDto1);

    List<RequestDto> findRequestsByUserIdAndEventId(Long userId, Long eventId);

    RequestDto confirmRequestByUserIdAndEventId(Long userId, Long eventId, Long reqId);

    RequestDto rejectRequestByUserIdAndEventId(Long userId, Long eventId, Long reqId);

    List<EventDto> getByParamFromAdmin(Long[] users, String[] states, Long[] categories, String rangeStart, String rangeEnd, Integer from, Integer size);

    EventDto updateByIdFromAdmin(Long eventId, NewEventDto newEventDto);

    EventDto publishEventByIdFromAdmin(Long eventId);

    EventDto rejectEventByIdFromAdmin(Long eventId);

}
