package ru.bykov.explore.services;

import ru.bykov.explore.model.Location;
import ru.bykov.explore.model.dto.LocationDto;

public interface LocationService {
    Location createLocation(LocationDto locationDto);
}
