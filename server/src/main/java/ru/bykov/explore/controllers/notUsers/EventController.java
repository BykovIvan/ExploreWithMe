package ru.bykov.explore.controllers.notUsers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.bykov.explore.model.dto.event.EventFullDto;
import ru.bykov.explore.model.dto.event.EventShortDto;
import ru.bykov.explore.services.EventService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class EventController {

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> allEventsByParam(@RequestParam(value = "text") String text,
                                                @RequestParam(value = "categories") Long[] categories,
                                                @RequestParam(value = "paid") Boolean paid,
                                                @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                                @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                                @RequestParam(value = "onlyAvailable", required = false, defaultValue = "false") Boolean onlyAvailable,//Default value : false
                                                @RequestParam(value = "sort") String sort,                                                             //Available values : EVENT_DATE, VIEWS
                                                @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,                      //Default value : 0
                                                @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,                     //Default value : 10
                                                HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту /events получение всех. Метод GET");
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        return eventService.findAllForAllUsers(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request.getRemoteAddr(), request.getRequestURI());
    }

    @GetMapping("/{eventId}")
    public EventFullDto eventById(@PathVariable("eventId") Long eventId,
                                  HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту /events получение по id. Метод GET");
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        return eventService.findByIdForAllUsers(eventId, request.getRemoteAddr(), request.getRequestURI());
    }

}
