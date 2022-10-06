package ru.bykov.explore.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bykov.explore.exceptions.NoParamInRequestException;
import ru.bykov.explore.exceptions.NotFoundException;
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
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;


    @Override
    public List<ParticipationRequestDto> findByUserIdFromUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Такого пользователя не существует!"));
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
    @Override
    public ParticipationRequestDto addRequestToEventByUserIdFromUser(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Такого пользователя не существует!"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Нет такого события!"));
        if (event.getInitiator().getId() == userId){
            throw new NoParamInRequestException("Пользователь не может подать запрос на участие в своем событии!");
        }
        if (event.getState().equals(StateOfEventAndReq.PENDING) || event.getState().equals(StateOfEventAndReq.CANCELED)){
            throw new NoParamInRequestException("Пользователь не может участвовать в неопубликованном событии!");
        }
        if (requestRepository.countByEvent(eventId) >= event.getParticipantLimit()){
            throw new NoParamInRequestException("Достигнут лимит запросов на участие!");
        }
        Request requestNew = Request.builder()
                .created(LocalDateTime.now())
                .event(eventId)
                .requester(userId)
                .status(StateOfEventAndReq.PENDING)
                .build();
        if (!event.getRequestModeration()){
            requestNew.setStatus(StateOfEventAndReq.PUBLISHED);
        }
        return RequestMapper.toParticipationRequestDto(requestRepository.save(requestNew));
    }

    @Override
    public ParticipationRequestDto canselRequestByUserIdAndRequestIdFromUser(Long userId, Long requestId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Такого пользователя не существует!"));
        requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Такой заявки не существует!"));

        return null;
    }
}
