package ru.bykov.explore.controllers.notUsers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.bykov.explore.model.dto.event.EventDto;
import ru.bykov.explore.model.dto.event.EventShortDto;
import ru.bykov.explore.services.EventService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class EventController {

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> allEvents(@RequestParam(value = "text") String text,
                                         @RequestParam(value = "categories") String[] categories,
                                         @RequestParam(value = "paid") Boolean paid,
                                         @RequestParam(value = "rangeStart") String rangeStart,
                                         @RequestParam(value = "rangeEnd") String rangeEnd,
                                         @RequestParam(value = "onlyAvailable", required = false, defaultValue = "false") Boolean onlyAvailable,//Default value : false
                                         @RequestParam(value = "sort") String sort,                                                             //Available values : EVENT_DATE, VIEWS
                                         @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,                      //Default value : 0
                                         @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {                   //Default value : 10
        log.info("Получен запрос к эндпоинту /events получение всех. Метод GET");
        return eventService.getAllForAllUsers(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{eventId}")
    public EventDto categoryById(@PathVariable("eventId") Long eventId) {
        log.info("Получен запрос к эндпоинту /events получение по id. Метод GET");
        return eventService.getByIdForAllUsers(eventId);
    }

}
