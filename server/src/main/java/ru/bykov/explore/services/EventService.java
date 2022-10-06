package ru.bykov.explore.services;

import ru.bykov.explore.model.dto.ParticipationRequestDto;
import ru.bykov.explore.model.dto.event.*;

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

    ParticipationRequestDto confirmRequestByUserIdAndEventIdFromUser(Long userId, Long eventId, Long reqId);

    ParticipationRequestDto rejectRequestByUserIdAndEventIdFromUser(Long userId, Long eventId, Long reqId);

    /**
     * Поиск события по параметрам администратором
     * Search for an event by parameters by the administrator
     */
    List<EventFullDto> getByParamFromAdmin(Long[] users, String[] states, Long[] categories, String rangeStart, String rangeEnd, Integer from, Integer size);

    /**
     * Редактирование события администратором
     * Editing an event by an administrator
     */
    EventFullDto updateByIdFromAdmin(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest);

    /**
     * Публикация события администратором
     * Publishing an event by an administrator
     */
    EventFullDto publishEventByIdFromAdmin(Long eventId);

    /**
     * Отклонение события администратором
     * Rejecting an event by an administrator
     */
    EventFullDto rejectEventByIdFromAdmin(Long eventId);

}
