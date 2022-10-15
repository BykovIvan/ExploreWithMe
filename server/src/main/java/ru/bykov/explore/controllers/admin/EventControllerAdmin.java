package ru.bykov.explore.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.bykov.explore.model.dto.event.AdminUpdateEventRequest;
import ru.bykov.explore.model.dto.event.EventFullDto;
import ru.bykov.explore.services.EventService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "admin/events")
public class EventControllerAdmin {

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> findByParam(@RequestParam(value = "users", required = false) Long[] users,            //список id пользователей, чьи события нужно найти
                                          @RequestParam(value = "states", required = false) String[] states,        //список состояний в которых находятся искомые события
                                          @RequestParam(value = "categories", required = false) Long[] categories,  //список id категорий в которых будет вестись поиск
                                          @RequestParam(value = "rangeStart", required = false) String rangeStart,  //дата и время не раньше которых должно произойти событие
                                          @RequestParam(value = "rangeEnd", required = false) String rangeEnd,      //дата и время не позже которых должно произойти событие
                                          @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
                                          @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        log.info("Получен запрос к эндпоинту /admin/events, метод GET, получение событий по параметрам users = {}, states = {}, categories = {}, " +
                "rangeStart = {}, rangeEnd = {}, from = {}, size = {}", users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.findByParamFromAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/{eventId}")
    public EventFullDto updateById(@PathVariable("eventId") Long eventId,
                                   @RequestBody AdminUpdateEventRequest adminUpdateEventRequest) {
        log.info("Получен запрос к эндпоинту /admin/events/{eventId}, метод PUT, обновление события по id = {}", eventId);
        return eventService.updateByIdFromAdmin(eventId, adminUpdateEventRequest);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable("eventId") Long eventId) {
        log.info("Получен запрос к эндпоинту /admin/events/{eventId}/publish, метод PATCH, публикация события по id = {}", eventId);
        return eventService.publishEventByIdFromAdmin(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable("eventId") Long eventId) {
        log.info("Получен запрос к эндпоинту /admin/events/{eventId}/reject, метод PATCH, отклонение публикации события по id = {}", eventId);
        return eventService.rejectEventByIdFromAdmin(eventId);
    }
}
