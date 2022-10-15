package ru.bykov.explore.services;

import ru.bykov.explore.model.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    /**
     * Получение информации о заявках текущего пользователя на участие в чужих событиях.
     * Obtaining information about the applications of the current user to participate in other people's events.
     */
    List<ParticipationRequestDto> findByUserIdFromUser(Long userId);

    /**
     * Добавление запроса от текущего пользователя на участие в событии.
     * Adding a request from the current user to participate in an event.
     */
    ParticipationRequestDto addRequestToEventByUserIdFromUser(Long userId, Long eventId);

    /**
     * Отмена своего запроса на участие в событии.
     * Cancel your event request.
     */
    ParticipationRequestDto canselRequestByUserIdAndRequestIdFromUser(Long userId, Long requestId);
}
