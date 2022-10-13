package ru.bykov.explore.services;

import ru.bykov.explore.model.dto.ParticipationRequestDto;
import ru.bykov.explore.model.dto.event.*;

import java.util.List;

public interface EventService {
    /**
     * Получение событий с возможностью фильтрации от всех пользователей
     * Receive filterable events from all users
     */
    List<EventShortDto> findAllForAllUsers(String text, Long[] categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size, String remoteAddr, String requestURI);

    /**
     * Получение подробной информации об опубликованном событии по его идентификатору
     * Getting detailed information about a published event by its ID
     */
    EventFullDto findByIdForAllUsers(Long eventId, String remoteAddr, String requestURI);

    /**
     * Получение событий, добавленных текущим пользователем
     * Getting events added by the current user
     */
    List<EventShortDto> findByUserIdFromUser(Long userId, Integer from, Integer size);

    /**
     * Изменение события добавленного текущим пользователем
     * Changing an event added by the current user
     */
    EventFullDto updateFromUser(Long userId, UpdateEventRequest updateEventRequest);

    /**
     * Добавление нового события текущим пользователем
     * Adding a new event by the current user
     */
    EventFullDto createFromUser(Long userId, NewEventDto newEventDto);

    /**
     * Получение полной информации о событии добавленном текущим пользователем
     * Getting full information about the event added by the current user
     */
    EventFullDto findByUserIdAndEventIdFromUser(Long userId, Long eventId);

    /**
     * Отмена события добавленного текущим пользователем
     * Cancel an event added by the current user
     */
    EventFullDto canselByUserIdAndEventIdFromUser(Long userId, Long eventId);

    /**
     * Получение информации о запросах на участие в событии текущего пользователя
     * Getting information about the current user's event participation requests
     */
    List<ParticipationRequestDto> findRequestsByUserIdAndEventIdFromUser(Long userId, Long eventId);

    /**
     * Подтверждение чужой заявки на участие в событии текущего пользователя
     * Confirmation of someone else's application for participation in the current user's event
     */
    ParticipationRequestDto confirmRequestByUserIdAndEventIdFromUser(Long userId, Long eventId, Long reqId);

    /**
     * Отклонение чужой заявки на участие в событии текущего пользователя
     * Rejection of someone else's application for participation in the current user's event
     */
    ParticipationRequestDto rejectRequestByUserIdAndEventIdFromUser(Long userId, Long eventId, Long reqId);

    /**
     * Поиск события по параметрам администратором
     * Search for an event by parameters by the administrator
     */
    List<EventFullDto> findByParamFromAdmin(Long[] users, String[] states, Long[] categories, String rangeStart, String rangeEnd, Integer from, Integer size);

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
