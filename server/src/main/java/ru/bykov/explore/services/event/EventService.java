package ru.bykov.explore.services.event;

import ru.bykov.explore.model.dto.CategoryDto;
import ru.bykov.explore.model.dto.event.EventShortDto;

import java.util.List;

public interface EventService {

    List<EventShortDto> getAllForAll();

    CategoryDto getByIdForAll(Long eventId);
}
