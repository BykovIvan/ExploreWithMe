package ru.bykov.explore.controllers.notUsers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bykov.explore.model.dto.CategoryDto;
import ru.bykov.explore.model.dto.event.EventShortDto;
import ru.bykov.explore.services.event.EventService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class EventController {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> allEvents() {
        log.info("Получен запрос к эндпоинту /events получение всех. Метод GET");
        return eventService.getAllForAll();
    }

    @GetMapping("/{eventId}")
    public CategoryDto categoryById(@PathVariable("eventId") Long eventId) {
        log.info("Получен запрос к эндпоинту /events получение по id. Метод GET");
        return eventService.getByIdForAll(eventId);
    }

}
