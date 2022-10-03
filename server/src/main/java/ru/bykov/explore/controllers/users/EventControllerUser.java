package ru.bykov.explore.controllers.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.bykov.explore.model.dto.event.EventFullDto;
import ru.bykov.explore.model.dto.ParticipationRequestDto;
import ru.bykov.explore.model.dto.event.EventShortDto;
import ru.bykov.explore.model.dto.event.UpdateEventRequest;
import ru.bykov.explore.services.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class EventControllerUser {

    private final EventService eventService;

    @GetMapping("/{userId}/events")
    public List<EventShortDto> userById(@PathVariable("userId") Long userId,
                                        HttpServletRequest request,
                                        @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
                                        @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        log.info("Получен запрос к эндпоинту /users/{userID}/events получение событий пользователя. Метод GET");
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        return eventService.findByUserIdFromUser(userId, request.getRemoteAddr(), request.getRequestURI(), from, size);
    }

    @PatchMapping("/{userId}/events")
    public EventFullDto updateByUser(@PathVariable("userId") Long userId,
                                     @Valid @RequestBody UpdateEventRequest updateEventRequest) {
        log.info("Получен запрос к эндпоинту /users/{userID}/events обновление события пользователем. Метод PATCH");
        return eventService.updateByUserIdFromUser(userId, updateEventRequest);
    }

    @PostMapping("/{userId}/events")
    public EventFullDto create(@PathVariable("userId") Long userId,
                               @RequestBody EventFullDto eventFullDto) {
        log.info("Получен запрос к эндпоинту /users/{userID}/events создание события пользователем. Метод POST");
        return eventService.createByUserId(userId, eventFullDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto eventById(@PathVariable("userId") Long userId,
                                  @PathVariable("eventId") Long eventId) {
        log.info("Получен запрос к эндпоинту /users/{userID}/events{eventId} получение события по id пользователя и id события. Метод GET");
        //нужна запись в статистику

        return eventService.findByUserIdAndEventId(userId, eventId);
    }

    //отмена события добавленного текущаим пользователем
    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto canselByUser(@PathVariable("userId") Long userId,
                                     @PathVariable("eventId") Long eventId,
                                     @RequestBody EventFullDto eventFullDto) {
        log.info("Получен запрос к эндпоинту /users/{userID}/events/{eventId} отмена события пользователем. Метод PATCH");
        return eventService.canselByUserIdAndEventId(userId, eventId, eventFullDto);
    }

    //Получение информации о запросах на участие в событиях текущего пользователя
    @GetMapping("/{userId}/events/{eventId}/request")
    public List<ParticipationRequestDto> requestEventById(@PathVariable("userId") Long userId,
                                                          @PathVariable("eventId") Long eventId) {
        log.info("Получен запрос к эндпоинту /users/{userID}/events{eventId}/request получение информации о запросах на участие в событиях. Метод GET");
        return eventService.findRequestsByUserIdAndEventId(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/request/{reqId}/confirm")
    public ParticipationRequestDto requestConfirmEventById(@PathVariable("userId") Long userId,
                                                           @PathVariable("eventId") Long eventId,
                                                           @PathVariable("reqId") Long reqId) {
        log.info("Получен запрос к эндпоинту /users/{userID}/events{eventId}/request/{reqId}/confirm подтверждение чужой заявки на участие в событии пользователем. Метод PATCH");
        return eventService.confirmRequestByUserIdAndEventId(userId, eventId, reqId);
    }

    @PatchMapping("/{userId}/events/{eventId}/request/{reqId}/reject")
    public ParticipationRequestDto requestRejectEventById(@PathVariable("userId") Long userId,
                                                          @PathVariable("eventId") Long eventId,
                                                          @PathVariable("reqId") Long reqId) {
        log.info("Получен запрос к эндпоинту /users/{userID}/events{eventId}/request/{reqId}/confirm отклонение чужой заявки на участие в событии пользователем. Метод PATCH");
        return eventService.rejectRequestByUserIdAndEventId(userId, eventId, reqId);
    }


}
