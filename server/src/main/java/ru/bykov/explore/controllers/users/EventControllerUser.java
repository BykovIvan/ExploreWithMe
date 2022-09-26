package ru.bykov.explore.controllers.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.bykov.explore.clientstat.StatClient;
import ru.bykov.explore.model.dto.event.EventDto;
import ru.bykov.explore.model.dto.event.EventRequestDto;
import ru.bykov.explore.services.EventService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class EventControllerUser {

    private final EventService eventService;

    @GetMapping("/{userId}/events")
    public List<EventDto> userById(@PathVariable("userId") Long userId) {
        log.info("Получен запрос к эндпоинту /users/{userID}/events получение событий пользователя. Метод GET");
        //нужна запись в статистику
//        statClient.createStat();
        return eventService.findByUserId(userId);
    }

    @PatchMapping("/{userId}/events")
    public EventDto updateByUser(@PathVariable("userId") Long userId,
                                 @RequestBody EventDto eventDto) {
        log.info("Получен запрос к эндпоинту /users/{userID}/events обновление события пользователем. Метод PATCH");
        return eventService.updateByUserId(userId, eventDto);
    }

    @PostMapping("/{userId}/events")
    public EventDto create(@PathVariable("userId") Long userId,
                           @RequestBody EventDto eventDto) {
        log.info("Получен запрос к эндпоинту /users/{userID}/events создание события пользователем. Метод POST");
        return eventService.createByUserId(userId, eventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventDto eventById(@PathVariable("userId") Long userId,
                              @PathVariable("eventId") Long eventId) {
        log.info("Получен запрос к эндпоинту /users/{userID}/events{eventId} получение события по id пользователя и id события. Метод GET");
        //нужна запись в статистику

        return eventService.findByUserIdAndEventId(userId, eventId);
    }

    //отмена события добавленного текущаим пользователем
    @PatchMapping("/{userId}/events/{eventId}")
    public EventDto canselByUser(@PathVariable("userId") Long userId,
                                 @PathVariable("eventId") Long eventId,
                                 @RequestBody EventDto eventDto) {
        log.info("Получен запрос к эндпоинту /users/{userID}/events/{eventId} отмена события пользователем. Метод PATCH");
        return eventService.canselByUserIdAndEventId(userId, eventId, eventDto);
    }

    //Получение информации о запросах на участие в событиях текущего пользователя
    @GetMapping("/{userId}/events/{eventId}/request")
    public List<EventRequestDto> requestEventById(@PathVariable("userId") Long userId,
                                                  @PathVariable("eventId") Long eventId) {
        log.info("Получен запрос к эндпоинту /users/{userID}/events{eventId}/request получение информации о запросах на участие в событиях. Метод GET");
        return eventService.findRequestsByUserIdAndEventId(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/request/{reqId}/confirm")
    public EventRequestDto requestConfirmEventById(@PathVariable("userId") Long userId,
                                                   @PathVariable("eventId") Long eventId,
                                                   @PathVariable("reqId") Long reqId) {
        log.info("Получен запрос к эндпоинту /users/{userID}/events{eventId}/request/{reqId}/confirm подтверждение чужой заявки на участие в событии пользователем. Метод PATCH");
        return eventService.confirmRequestByUserIdAndEventId(userId, eventId, reqId);
    }

    @PatchMapping("/{userId}/events/{eventId}/request/{reqId}/reject")
    public EventRequestDto requestRejectEventById(@PathVariable("userId") Long userId,
                                                  @PathVariable("eventId") Long eventId,
                                                  @PathVariable("reqId") Long reqId) {
        log.info("Получен запрос к эндпоинту /users/{userID}/events{eventId}/request/{reqId}/confirm отклонение чужой заявки на участие в событии пользователем. Метод PATCH");
        return eventService.rejectRequestByUserIdAndEventId(userId, eventId, reqId);
    }


}
