package ru.bykov.explore.services;

import ru.bykov.explore.model.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    List<ParticipationRequestDto> findByUserId(Long userId);

    ParticipationRequestDto addRequestToEventByUserId(Long userId, Long eventId);

    ParticipationRequestDto canselRequestByUserIdAndRequestId(Long userId, Long requestId);
}
