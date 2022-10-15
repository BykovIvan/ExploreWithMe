package ru.bykov.explore.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bykov.explore.exceptions.EntityNotFoundException;
import ru.bykov.explore.exceptions.ValidationException;
import ru.bykov.explore.model.Event;
import ru.bykov.explore.model.Request;
import ru.bykov.explore.model.User;
import ru.bykov.explore.model.dto.ParticipationRequestDto;
import ru.bykov.explore.repositories.EventRepository;
import ru.bykov.explore.repositories.RequestRepository;
import ru.bykov.explore.repositories.UserRepository;
import ru.bykov.explore.services.RequestService;
import ru.bykov.explore.utils.EventState;
import ru.bykov.explore.utils.RequestState;
import ru.bykov.explore.utils.mapperForDto.RequestMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;


    @Override
    public List<ParticipationRequestDto> findByUserIdFromUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", userId.toString()));
        return requestRepository.findByRequesterId(userId)
                .stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto addRequestToEventByUserIdFromUser(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", userId.toString()));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString()));
        if (event.getInitiator().getId() == userId)
            throw new ValidationException(Event.class, "Initiator", event.getInitiator() + "! Пользователь" +
                    " не может подать запрос на участие в своем событии!");
        if (!event.getState().equals(EventState.PUBLISHED))
            throw new ValidationException(Event.class, "State", event.getState() + "! Пользователь " +
                    "не может участвовать в неопубликованном событии!");
        if (event.getParticipantLimit() != 0 && requestRepository.countByEventId(eventId) >= event.getParticipantLimit()) {
            throw new ValidationException(Event.class, "Сount", requestRepository.countByEventId(eventId) + "! Достигнут лимит запросов на участие!");
        }
        Request requestNew = Request.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(user)
                .status(RequestState.PENDING)
                .build();
        if (!event.getRequestModeration() && requestRepository.countByEventId(eventId) < event.getParticipantLimit()) {
            requestNew.setStatus(RequestState.CONFIRMED);
        }
        return RequestMapper.toParticipationRequestDto(requestRepository.save(requestNew));
    }

    @Override
    @Transactional
    public ParticipationRequestDto canselRequestByUserIdAndRequestIdFromUser(Long userId, Long requestId) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", userId.toString()));
        Request request = requestRepository.findByIdAndRequesterId(requestId, userId);
        if (request == null) throw new EntityNotFoundException(Request.class, "id", requestId.toString());
        if (request.getStatus().equals(RequestState.CONFIRMED) && eventRepository.findById(request.getEvent().getId()).get().getConfirmedRequests() != 0)
            eventRepository.setNewConfirmedRequestsMinusOne(request.getEvent().getId());
        request.setStatus(RequestState.CANCELED);
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }
}
