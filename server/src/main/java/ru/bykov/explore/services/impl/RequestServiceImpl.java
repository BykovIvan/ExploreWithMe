package ru.bykov.explore.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bykov.explore.exceptions.NotFoundException;
import ru.bykov.explore.services.RequestService;
import ru.bykov.explore.model.dto.RequestDto;
import ru.bykov.explore.repositories.RequestRepository;
import ru.bykov.explore.repositories.UserRepository;
import ru.bykov.explore.utils.mapperForDto.RequestMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    //Обратите внимание:
    //
    //нельзя добавить повторный запрос
    //инициатор события не может добавить запрос на участие в своём событии
    //нельзя участвовать в неопубликованном событии
    //если у события достигнут лимит запросов на участие - необходимо вернуть ошибку
    //если для события отключена пре-модерация запросов на участие, то запрос должен автоматически перейти в состояние подтвержденного
    @Override
    public List<RequestDto> findByUserId(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Такого пользователя не существует!"));
        return requestRepository.findByRequester(userId).stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }
}
