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
import ru.bykov.explore.utils.StateOfEventAndReq;
import ru.bykov.explore.utils.mapperForDto.RequestMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;


    @Override
    public List<ParticipationRequestDto> findByUserIdFromUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", userId.toString()));
        return requestRepository.findByRequester(userId)
                .stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    //Обратите внимание:
    //нельзя добавить повторный запрос
    //инициатор события не может добавить запрос на участие в своём событии
    //нельзя участвовать в неопубликованном событии
    //если у события достигнут лимит запросов на участие - необходимо вернуть ошибку
    //если для события отключена пре-модерация запросов на участие, то запрос должен автоматически перейти в состояние подтвержденного
    @Transactional
    @Override
    public ParticipationRequestDto addRequestToEventByUserIdFromUser(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", userId.toString()));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", eventId.toString()));
        if (event.getInitiator().getId() == userId) {
            throw new ValidationException(Event.class, "Initiator = " + event.getInitiator(), "Пользователь не может подать запрос на участие в своем событии!");
        }
        if (event.getState().equals(StateOfEventAndReq.PENDING) || event.getState().equals(StateOfEventAndReq.CANCELED)) {
            //TODO может разделить
            throw new ValidationException(Event.class, "State = " + event.getState(), "Пользователь не может участвовать в неопубликованном событии!");
        }
        if (requestRepository.countByEvent(eventId) >= event.getParticipantLimit()) {
            throw new ValidationException(Event.class, "Сount = " + requestRepository.countByEvent(eventId), "Достигнут лимит запросов на участие!");
        }
        Request requestNew = Request.builder()
                .created(LocalDateTime.now())
                .event(eventId)
                .requester(userId)
                .status(StateOfEventAndReq.PENDING)
                .build();
        if (!event.getRequestModeration()) {
            requestNew.setStatus(StateOfEventAndReq.PUBLISHED);
        }
        return RequestMapper.toParticipationRequestDto(requestRepository.save(requestNew));
    }

    @Transactional
    @Override
    public ParticipationRequestDto canselRequestByUserIdAndRequestIdFromUser(Long userId, Long requestId) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, "id", userId.toString()));
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new EntityNotFoundException(Request.class, "id", requestId.toString()));
        if (request.getRequester() != userId) {
            throw new ValidationException(Request.class, "Initiator = " + request.getRequester(), "Данный пользователь не является владельцем этой заявки!");
        }
        if (request.getStatus().equals(StateOfEventAndReq.PUBLISHED)) {
            Event event = eventRepository.findById(request.getEvent()).orElseThrow(() -> new EntityNotFoundException(Event.class, "id", request.getEvent().toString()));
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            eventRepository.save(event);
        }
        request.setStatus(StateOfEventAndReq.CANCELED);
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }
}
