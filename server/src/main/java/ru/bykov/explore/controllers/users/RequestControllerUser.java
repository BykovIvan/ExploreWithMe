package ru.bykov.explore.controllers.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.bykov.explore.model.dto.ParticipationRequestDto;
import ru.bykov.explore.services.RequestService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class RequestControllerUser {

    private final RequestService requestService;

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> findByUserId(@PathVariable("userId") Long userId) {
        log.info("Получен запрос к эндпоинту /users/{userID}/request получение информации о заявках текущего пользователя с id = {} на участие в чужих событиях", userId);
        return requestService.findByUserIdFromUser(userId);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addRequest(@PathVariable("userId") Long userId,
                                              @RequestParam(value = "eventId") Long eventId) {
        log.info("Получен запрос к эндпоинту /users/{userID}/request добаление запроса от пользователя id = {} на участие в событии id = {}", userId, eventId);
        return requestService.addRequestToEventByUserIdFromUser(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto canselRequest(@PathVariable("userId") Long userId,
                                                 @PathVariable("requestId") Long requestId) {
        log.info("Получен запрос к эндпоинту /users/{userID}/request/{requestId}/cansel отмена запроса от пользователя id = {} на участие с id = {}", userId, requestId);
        return requestService.canselRequestByUserIdAndRequestIdFromUser(userId, requestId);
    }

}
