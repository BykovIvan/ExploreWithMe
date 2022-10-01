package ru.bykov.explore.controllers.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.bykov.explore.services.RequestService;
import ru.bykov.explore.model.dto.RequestDto;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class RequestControllerUser {

    private final RequestService requestService;

    @GetMapping("/{userId}/request")
    public List<RequestDto> findByUserId(@PathVariable("userId") Long userId) {
        log.info("Получен запрос к эндпоинту /users/{userID}/request получение информации о заявках текущего пользователя с id = {} на участие в чужих событиях", userId);
        return requestService.findByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto addRequest()

}
