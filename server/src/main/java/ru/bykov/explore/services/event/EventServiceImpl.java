package ru.bykov.explore.services.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bykov.explore.model.dto.CategoryDto;
import ru.bykov.explore.model.dto.event.EventShortDto;
import ru.bykov.explore.repositories.EventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    public List<EventShortDto> getAllForAll() {
        return null;
    }

    @Override
    public CategoryDto getByIdForAll(Long eventId) {
        return null;
    }
}
