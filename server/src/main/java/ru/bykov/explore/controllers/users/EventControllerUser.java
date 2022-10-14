package ru.bykov.explore.controllers.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.bykov.explore.model.dto.ParticipationRequestDto;
import ru.bykov.explore.model.dto.event.EventFullDto;
import ru.bykov.explore.model.dto.event.EventShortDto;
import ru.bykov.explore.model.dto.event.NewEventDto;
import ru.bykov.explore.model.dto.event.UpdateEventRequest;
import ru.bykov.explore.services.EventService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class EventControllerUser {

    private final EventService eventService;

    @GetMapping("/{userId}/events")
    public List<EventShortDto> eventsByUserById(@PathVariable("userId") Long userId,
                                                @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
                                                @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        log.info("Получен запрос к эндпоинту /users/{userID}/events получение событий пользователя. Метод GET");
        return eventService.findByUserIdFromUser(userId, from, size);
    }

    @PatchMapping("/{userId}/events")
    public EventFullDto updateEventFromUser(@PathVariable("userId") Long userId,
                                            @Valid @RequestBody UpdateEventRequest updateEventRequest) {
        log.info("Получен запрос к эндпоинту /users/{userID}/events обновление события пользователем. Метод PATCH");
        return eventService.updateFromUser(userId, updateEventRequest);
    }

    @PostMapping("/{userId}/events")
//    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createFromUser(@PathVariable("userId") Long userId,
                                       @Valid @RequestBody NewEventDto newEventDto) {
        log.info("Получен запрос к эндпоинту /users/{userID}/events создание события пользователем. Метод POST");
        return eventService.createFromUser(userId, newEventDto);
    }

    //Получение полной информации о событии добавленном текущим пользователем
    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto eventByUserIdAndEventIdFromUser(@PathVariable("userId") Long userId,
                                                        @PathVariable("eventId") Long eventId) {
        log.info("Получен запрос к эндпоинту /users/{userID}/events{eventId} получение события по id пользователя и id события. Метод GET");
        return eventService.findByUserIdAndEventIdFromUser(userId, eventId);
    }

    //отмена события добавленного текущим пользователем
    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto canselByUser(@PathVariable("userId") Long userId,
                                     @PathVariable("eventId") Long eventId) {
        log.info("Получен запрос к эндпоинту /users/{userID}/events/{eventId} отмена события пользователем. Метод PATCH");
        return eventService.canselByUserIdAndEventIdFromUser(userId, eventId);
    }

    //Получение информации о запросах на участие в событиях текущего пользователя
    @GetMapping("/{userId}/events/{eventId}/request")
    public List<ParticipationRequestDto> requestEventById(@PathVariable("userId") Long userId,
                                                          @PathVariable("eventId") Long eventId) {
        log.info("Получен запрос к эндпоинту /users/{userID}/events{eventId}/request получение информации о запросах на участие в событиях. Метод GET. Где userId = {}, eventId={}", userId, eventId);
        return eventService.findRequestsByUserIdAndEventIdFromUser(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/request/{reqId}/confirm")
    public ParticipationRequestDto requestConfirmEventById(@PathVariable("userId") Long userId,
                                                           @PathVariable("eventId") Long eventId,
                                                           @PathVariable("reqId") Long reqId) {
        log.info("Получен запрос к эндпоинту /users/{userID}/events{eventId}/request/{reqId}/confirm подтверждение чужой заявки на участие в событии пользователем. Метод PATCH");
        return eventService.confirmRequestByUserIdAndEventIdFromUser(userId, eventId, reqId);
    }

    @PatchMapping("/{userId}/events/{eventId}/request/{reqId}/reject")
    public ParticipationRequestDto requestRejectEventById(@PathVariable("userId") Long userId,
                                                          @PathVariable("eventId") Long eventId,
                                                          @PathVariable("reqId") Long reqId) {
        log.info("Получен запрос к эндпоинту /users/{userID}/events{eventId}/request/{reqId}/confirm отклонение чужой заявки на участие в событии пользователем. Метод PATCH");
        return eventService.rejectRequestByUserIdAndEventIdFromUser(userId, eventId, reqId);
    }


}
